package pm.gh.integration.rest.mapper

import pm.gh.integration.model.Project
import pm.gh.integration.rest.dto.ProjectDto
import pm.gh.integration.rest.dto.ProjectUpdateDto
import pm.gh.integration.util.toObjectId

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