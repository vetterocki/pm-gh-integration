package pm.gh.integration.infrastructure.rest.dto

data class TicketDto(
    val projectId: String,
    val summary: String,
    val description: String?,
    val reporterId: String,
    val assigneeId: String,
    val linkedTicketIds: List<String>?,
    val priority: String,
    val status: String,
)