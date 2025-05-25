package pm.gh.integration.infrastructure.rest.dto

data class ProjectBoardDto(
    val id: String?,
    val name: String,
    val projectId: String,
    val ticketIds: List<String>?,
)