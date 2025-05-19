package pm.gh.integration.rest.dto

data class TeamMemberDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val teamId: String,
    val position: String,
)