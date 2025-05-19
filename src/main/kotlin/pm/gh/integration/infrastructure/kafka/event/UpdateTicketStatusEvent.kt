package pm.gh.integration.infrastructure.kafka.event

data class UpdateTicketStatusEvent(
    val status: String,
    val titleComposition: TitleComposition
)