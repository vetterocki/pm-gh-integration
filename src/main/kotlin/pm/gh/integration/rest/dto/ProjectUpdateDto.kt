package pm.gh.integration.rest.dto

data class ProjectUpdateDto(
    val key: String?,
    val fullName: String?,
    val teamId: String?,
    val projectOwnerId: String?,
)