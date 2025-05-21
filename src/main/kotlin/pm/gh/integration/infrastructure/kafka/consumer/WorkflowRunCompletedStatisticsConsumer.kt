package pm.gh.integration.infrastructure.kafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.infrastructure.kafka.mapper.WorkflowRunMapper.toDomain
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.output.WorkflowRunCompletedEvent
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class WorkflowRunCompletedStatisticsConsumer(
    @Qualifier("workflowRunCompletedStatisticsReceiver")
    private val kafkaReceiver: KafkaReceiver<String, ByteArray>,
    private val ticketService: TicketService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun listenToWorkflowRunCompletedTopic() {
        kafkaReceiver.receive()
            .flatMap { receiverRecord ->
                receiverRecord.toMono()
                    .map { WorkflowRunCompletedEvent.parseFrom(it.value()) }
                    .flatMap { addWorkflowRunToTicket(it) }
                    .doFinally { receiverRecord.receiverOffset().acknowledge() }
            }
            .subscribe()
    }

    private fun addWorkflowRunToTicket(workflowRunCompletedEvent: WorkflowRunCompletedEvent): Mono<Ticket> {
        return workflowRunCompletedEvent.run {
            ticketService.updateTicketWorkflowRuns(
                titleComposition.ticketIdentifier,
                toDomain()
            )
                .switchIfEmpty {
                    Mono.defer {
                        logger.warn(
                            "No ticket {} found, skipping workflow run adding to ticket {}",
                            titleComposition,
                            toDomain()
                        )
                        Mono.empty()
                    }
                }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(WorkflowRunCompletedStatisticsConsumer::class.java)
    }
}