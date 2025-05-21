package pm.gh.integration.infrastructure.rest.dto

data class ProjectBoardDto(
    val name: String,
    val projectId: String,
    val ticketIds: List<String>?,
)