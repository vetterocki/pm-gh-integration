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
            teamId = teamId.toString(),
            projectOwnerId = projectOwnerId.toString(),
            projectLabelIds = projectLabelIds?.map { it.toString() },
            key = key
        )
    }

    fun ProjectDto.toModel(): Project {
        return Project(
            id = null,
            fullName = fullName,
            projectBoardIds = projectBoardIds?.map { it.toObjectId() },
            teamId = teamId.toObjectId(),
            projectOwnerId = projectOwnerId.toObjectId(),
            projectLabelIds = projectLabelIds?.map { it.toObjectId() },
            key = key
        )
    }

    fun Project.partialUpdate(updatedProject: ProjectUpdateDto): Project {
        return copy(
            fullName = updatedProject.fullName ?: fullName,
            projectOwnerId = updatedProject.projectOwnerId?.toObjectId() ?: projectOwnerId,
            teamId = updatedProject.teamId?.toObjectId() ?: teamId,
            key = updatedProject.key ?: key
        )
    }
}