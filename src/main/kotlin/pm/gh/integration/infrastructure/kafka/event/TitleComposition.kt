package pm.gh.integration.infrastructure.kafka.event

data class TitleComposition(
    val projectKey: String?,
    val ticketIdentifier: String?,
    val ticketSummary: String
)