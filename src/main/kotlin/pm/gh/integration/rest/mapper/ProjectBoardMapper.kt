package pm.gh.integration.rest.mapper

import pm.gh.integration.model.ProjectBoard
import pm.gh.integration.rest.dto.ProjectBoardDto
import pm.gh.integration.util.toObjectId

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