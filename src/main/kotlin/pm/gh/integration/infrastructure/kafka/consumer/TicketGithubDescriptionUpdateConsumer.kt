package pm.gh.integration.infrastructure.kafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.input.UpdateTicketGithubDescriptionEvent
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class TicketGithubDescriptionUpdateConsumer(
    @Qualifier("updateTicketGithubDescriptionReceiver")
    private val kafkaReceiver: KafkaReceiver<String, ByteArray>,
    private val ticketService: TicketService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun listenToTicketUpdateGithubDescriptionTopic() {
        kafkaReceiver.receive()
            .flatMap { receiverRecord ->
                receiverRecord.toMono()
                    .map { UpdateTicketGithubDescriptionEvent.parseFrom(it.value()) }
                    .flatMap { performGithubDescriptionUpdate(it) }
                    .doFinally { receiverRecord.receiverOffset().acknowledge() }

            }
            .subscribe()
    }

    private fun performGithubDescriptionUpdate(event: UpdateTicketGithubDescriptionEvent): Mono<Ticket> {
        return event.run {
            ticketService.updateTicketGithubDescription(
                ticketIdentifier = titleComposition.ticketIdentifier,
                description
            ).switchIfEmpty {
                Mono.defer {
                    logger.warn(
                        "No ticket {} found, skipping updating ticket github description {}",
                        titleComposition,
                        description
                    )
                    Mono.empty()
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TicketGithubDescriptionUpdateConsumer::class.java)
    }
}