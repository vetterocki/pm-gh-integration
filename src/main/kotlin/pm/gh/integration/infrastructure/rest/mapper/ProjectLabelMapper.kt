package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto

object ProjectLabelMapper {
    fun ProjectLabel.toDto(): ProjectLabelDto = ProjectLabelDto(
        id = id.toString(),
        name = name,
        color = color,
        description = description.orEmpty(),
        projectId = projectId.toString(),
    )

    fun ProjectLabelDto.toModel(): ProjectLabel = ProjectLabel(
        id = null,
        name = name.orEmpty(),
        color = color.orEmpty(),
        description = description.orEmpty(),
        projectId = projectId?.toObjectId()
    )

    fun ProjectLabel.partialUpdate(projectLabelDto: ProjectLabelDto): ProjectLabel = copy(
        name = projectLabelDto.name ?: name,
        color = projectLabelDto.color ?: color,
        description = projectLabelDto.description ?: description,
    )
}