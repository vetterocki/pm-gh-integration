package pm.gh.integration.infrastructure.rest.dto

import org.bson.types.ObjectId

data class ProjectLabelDto(
    val id: String?,
    val name: String?,
    val color: String?,
    val description: String?,
    val projectId: String?,
)