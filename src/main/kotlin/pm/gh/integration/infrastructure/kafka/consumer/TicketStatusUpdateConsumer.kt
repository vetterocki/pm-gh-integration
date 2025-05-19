package pm.gh.integration.infrastructure.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import pm.gh.integration.infrastructure.kafka.KafkaTopic
import pm.gh.integration.infrastructure.kafka.KafkaTopic.TicketStatus.UPDATE_TOPIC
import pm.gh.integration.infrastructure.kafka.event.UpdateTicketStatusEvent

class TicketStatusUpdateConsumer {

    @KafkaListener(topics = [UPDATE_TOPIC])
    fun listen(updateTicketStatusEvent: UpdateTicketStatusEvent) {
        println(updateTicketStatusEvent)
    }
}