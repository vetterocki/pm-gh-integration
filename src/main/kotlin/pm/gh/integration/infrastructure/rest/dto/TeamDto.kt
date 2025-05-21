package pm.gh.integration.infrastructure.rest.dto

data class TeamDto(
    val name: String,
    val projectManagerId: String?,
    val teamMemberIds: List<String>?,
    val projectIds: List<String>?,
)