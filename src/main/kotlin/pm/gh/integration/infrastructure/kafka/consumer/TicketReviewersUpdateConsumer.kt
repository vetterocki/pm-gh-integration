package pm.gh.integration.infrastructure.kafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.infrastructure.kafka.mapper.ActorMapper.toDomain
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.input.UpdateTicketReviewersEvent
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class TicketReviewersUpdateConsumer(
    @Qualifier("updateTicketReviewersReceiver")
    private val kafkaReceiver: KafkaReceiver<String, ByteArray>,
    private val ticketService: TicketService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun listenToTicketUpdateReviewersTopic() {
        kafkaReceiver.receive()
            .flatMap { receiverRecord ->
                receiverRecord.toMono()
                    .map { UpdateTicketReviewersEvent.parseFrom(it.value()) }
                    .flatMap { performReviewersUpdate(it) }
                    .doFinally { receiverRecord.receiverOffset().acknowledge() }

            }
            .subscribe()
    }

    private fun performReviewersUpdate(event: UpdateTicketReviewersEvent): Mono<Ticket> {
        return event.run {
            ticketService.updateTicketReviewers(
                ticketIdentifier = titleComposition.ticketIdentifier,
                reviewers = reviewersList.map { it.toDomain() }
            ).switchIfEmpty {
                Mono.defer {
                    logger.warn(
                        "No ticket {} found, skipping updating ticket reviewers {}",
                        titleComposition,
                        reviewersList.map { it.toDomain() }
                    )
                    Mono.empty()
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TicketReviewersUpdateConsumer::class.java)
    }
}