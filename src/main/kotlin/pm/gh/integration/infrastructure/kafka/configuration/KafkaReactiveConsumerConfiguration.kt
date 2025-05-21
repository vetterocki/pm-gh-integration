package pm.gh.integration.infrastructure.kafka.configuration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pm.gh.integration.KafkaConsumerGroup
import pm.gh.integration.KafkaTopic
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Configuration
class KafkaReactiveConsumerConfiguration(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    private val kafkaProperties: KafkaProperties,
) {
    @Bean
    fun updateTicketStatusReceiver(): KafkaReceiver<String, ByteArray> {
        return KafkaReceiver.create(
            createConsumerProperties(
                consumerGroup = KafkaConsumerGroup.UPDATE_TICKET_STATUS_GROUP,
                topic = KafkaTopic.TicketStatus.UPDATE_TOPIC
            )
        )
    }

    @Bean
    fun updateTicketReviewersReceiver(): KafkaReceiver<String, ByteArray> {
        return KafkaReceiver.create(
            createConsumerProperties(
                consumerGroup = KafkaConsumerGroup.UPDATE_TICKET_REVIEWERS_GROUP,
                topic = KafkaTopic.TicketReviewers.UPDATE_TOPIC
            )
        )
    }

    @Bean
    fun updateTicketGithubDescriptionReceiver(): KafkaReceiver<String, ByteArray> {
        return KafkaReceiver.create(
            createConsumerProperties(
                consumerGroup = KafkaConsumerGroup.UPDATE_TICKET_GITHUB_DESCRIPTION_GROUP,
                topic = KafkaTopic.TicketGithubDescription.UPDATE_TOPIC
            )
        )
    }

    @Bean
    fun pullRequestStatusReceiver(): KafkaReceiver<String, ByteArray> {
        return KafkaReceiver.create(
            createConsumerProperties(
                consumerGroup = KafkaConsumerGroup.PULL_REQUEST_STATUS_GROUP,
                topic = KafkaTopic.PullRequest.STATUS_TOPIC
            )
        )
    }

    @Bean
    fun workflowRunCompletedStatisticsReceiver(): KafkaReceiver<String, ByteArray> {
        return KafkaReceiver.create(
            createConsumerProperties(
                consumerGroup = KafkaConsumerGroup.WORKFLOW_RUN_COMPLETED_GROUP,
                topic = KafkaTopic.WorkflowRun.COMPLETED_TOPIC
            )
        )
    }

    private fun createConsumerProperties(
        consumerGroup: String,
        topic: String,
    ): ReceiverOptions<String, ByteArray> {
        val properties = kafkaProperties.consumer.buildProperties(null).apply {
            putAll(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ByteArrayDeserializer::class.java,
                    ConsumerConfig.GROUP_ID_CONFIG to consumerGroup
                )
            )
        }
        return ReceiverOptions.create<String, ByteArray>(properties).subscription(setOf(topic))
    }
}