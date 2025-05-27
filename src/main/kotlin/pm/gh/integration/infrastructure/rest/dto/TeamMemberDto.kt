package pm.gh.integration.infrastructure.rest.dto

data class TeamMemberDto(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val teamId: String,
    val position: String,
    val loginInGithub: String,
    val avatarUrl: String?
)