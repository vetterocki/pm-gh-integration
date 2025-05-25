package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.rest.dto.TeamDto
import pm.gh.integration.infrastructure.rest.dto.TeamUpdateDto

object TeamMapper {
    fun Team.toDto(): TeamDto {
        return TeamDto(
            name = name,
            teamMemberIds = teamMemberIds?.map { it.toString() },
            projectIds = projectIds?.map { it.toString() },
            id = id.toString(),
            projectManagerName = projectManager?.fullName.orEmpty()
        )
    }


    fun TeamDto.toModel(): Team {
        return Team(
            name = name,
            teamMemberIds = teamMemberIds?.map { it.toObjectId() },
            projectIds = projectIds?.map { it.toObjectId() },
            id = null,
            projectManager = null
        )
    }

    fun Team.partialUpdate(teamUpdateDto: TeamUpdateDto): Team {
        return copy(
            name = teamUpdateDto.name ?: name,
        )
    }
}