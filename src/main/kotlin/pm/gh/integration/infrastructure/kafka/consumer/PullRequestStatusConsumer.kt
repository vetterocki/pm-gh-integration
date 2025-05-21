package pm.gh.integration.infrastructure.kafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.infrastructure.kafka.mapper.PullRequestMapper.toDomain
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.output.PullRequestStatusEvent
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class PullRequestStatusConsumer(
    @Qualifier("pullRequestStatusReceiver")
    private val kafkaReceiver: KafkaReceiver<String, ByteArray>,
    private val ticketService: TicketService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun listenToPullRequestStatusTopic() {
        kafkaReceiver.receive()
            .flatMap { receiverRecord ->
                receiverRecord.toMono()
                    .map { PullRequestStatusEvent.parseFrom(it.value()) }
                    .flatMap { addPullRequestToTicket(it) }
                    .doFinally { receiverRecord.receiverOffset().acknowledge() }
            }
            .subscribe()
    }

    private fun addPullRequestToTicket(pullRequestStatusEvent: PullRequestStatusEvent): Mono<Ticket> {
        return pullRequestStatusEvent.run {
            ticketService.updateTicketPullRequests(
                titleComposition.ticketIdentifier,
                toDomain()
            )
                .switchIfEmpty {
                    Mono.defer {
                        logger.warn(
                            "No ticket for {} found, skipping pull request adding to ticket {}",
                            titleComposition,
                            toDomain()
                        )
                        Mono.empty()
                    }
                }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PullRequestStatusConsumer::class.java)
    }
}