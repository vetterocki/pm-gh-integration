package pm.gh.integration.infrastructure.rest.dto

data class TeamMemberUpdateDto (
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val teamId: String?,
    val position: String?,
    val loginInGithub: String?,
    val avatarUrl: String?
)
