package pm.gh.integration.rest.dto

data class TeamMemberUpdateDto (
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val teamId: String?,
    val position: String?,
)
