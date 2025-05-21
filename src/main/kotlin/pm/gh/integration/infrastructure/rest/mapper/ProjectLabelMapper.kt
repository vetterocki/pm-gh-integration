package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto

object ProjectLabelMapper {
    fun ProjectLabel.toDto(): ProjectLabelDto = ProjectLabelDto(name)

    fun ProjectLabelDto.toModel(): ProjectLabel = ProjectLabel(id = null, name = name)

    fun ProjectLabel.partialUpdate(projectLabelDto: ProjectLabelDto): ProjectLabel = copy(name = projectLabelDto.name)
}