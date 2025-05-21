package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.ProjectBoard
import pm.gh.integration.infrastructure.rest.dto.ProjectBoardDto

object ProjectBoardMapper {
    fun ProjectBoard.toDto(): ProjectBoardDto {
        return ProjectBoardDto(
            name = name,
            projectId = projectId.toString(),
            ticketIds = ticketIds?.map { it.toString() }
        )
    }

    fun ProjectBoardDto.toModel(): ProjectBoard {
        return ProjectBoard(
            id = null,
            name = name,
            projectId = projectId.toObjectId(),
            ticketIds = ticketIds?.map { it.toObjectId() }
        )
    }

    fun ProjectBoard.partialUpdate(projectBoardDto: ProjectBoardDto): ProjectBoard {
        return copy(name = projectBoardDto.name)
    }
}