package pm.gh.integration.infrastructure.rest.dto

data class TicketCreateDto(
    val projectId: String,
    val projectBoardId: String,
    val summary: String,
    val description: String?,
    val reporterId: String,
    val linkedTicketIds: List<String>?,
    val priority: String,
    val labels: List<String>?,
)