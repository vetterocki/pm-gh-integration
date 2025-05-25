package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.rest.dto.ProjectDto
import pm.gh.integration.infrastructure.rest.dto.ProjectUpdateDto

object ProjectMapper {
    fun Project.toDto(): ProjectDto {
        return ProjectDto(
            fullName = fullName,
            projectBoardIds = projectBoardIds?.map { it.toString() },
            projectLabelIds = projectLabelIds?.map { it.toString() },
            key = key,
            teamName = team?.name.orEmpty(),
            projectOwnerName = projectOwner?.fullName.orEmpty(),
            id = id.toString()
        )
    }

    fun ProjectDto.toModel(): Project {
        return Project(
            id = null,
            fullName = fullName,
            projectBoardIds = projectBoardIds?.map { it.toObjectId() },
            team = null,
            projectOwner = null,
            projectLabelIds = projectLabelIds?.map { it.toObjectId() },
            key = key
        )
    }

    fun Project.partialUpdate(updatedProject: ProjectUpdateDto): Project {
        return copy(
            fullName = updatedProject.fullName ?: fullName,
            key = updatedProject.key ?: key,
        )
    }
}