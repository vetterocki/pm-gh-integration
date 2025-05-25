package pm.gh.integration.infrastructure.rest.dto

data class TeamDto(
    val id: String?,
    val name: String,
    val projectManagerName: String,
    val teamMemberIds: List<String>?,
    val projectIds: List<String>?,
)