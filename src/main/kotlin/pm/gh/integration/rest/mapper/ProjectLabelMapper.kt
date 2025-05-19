package pm.gh.integration.rest.mapper

import pm.gh.integration.model.ProjectLabel
import pm.gh.integration.rest.dto.ProjectLabelDto

object ProjectLabelMapper {
    fun ProjectLabel.toDto(): ProjectLabelDto = ProjectLabelDto(name)

    fun ProjectLabelDto.toModel(): ProjectLabel = ProjectLabel(id = null, name = name)

    fun ProjectLabel.partialUpdate(projectLabelDto: ProjectLabelDto): ProjectLabel = copy(name = projectLabelDto.name)
}