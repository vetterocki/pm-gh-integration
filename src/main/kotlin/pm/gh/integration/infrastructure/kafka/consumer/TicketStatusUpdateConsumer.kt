package pm.gh.integration.infrastructure.kafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.input.UpdateTicketStatusEvent
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class TicketStatusUpdateConsumer(
    @Qualifier("updateTicketStatusReceiver")
    private val kafkaReceiver: KafkaReceiver<String, ByteArray>,
    private val ticketService: TicketService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun listenToTicketUpdateStatusTopic() {
        kafkaReceiver.receive()
            .flatMap { receiverRecord ->
                receiverRecord.toMono()
                    .map { UpdateTicketStatusEvent.parseFrom(it.value()) }
                    .flatMap { performStatusUpdate(it) }
                    .doFinally { receiverRecord.receiverOffset().acknowledge() }

            }
            .subscribe()
    }

    private fun performStatusUpdate(updateTicketStatusEvent: UpdateTicketStatusEvent): Mono<Ticket> {
        println(updateTicketStatusEvent)
        return updateTicketStatusEvent.run {
            ticketService.updateTicketStatus(
                ticketIdentifier = titleComposition.ticketIdentifier,
                status = status
            ).switchIfEmpty {
                Mono.defer {
                    logger.warn(
                        "No ticket \n {} found, skipping updating ticket status {}",
                        titleComposition,
                        status
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