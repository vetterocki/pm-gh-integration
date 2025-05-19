package pm.gh.integration.rest.mapper

import io.github.serpro69.kfaker.provider.Tea
import pm.gh.integration.model.Project
import pm.gh.integration.model.Team
import pm.gh.integration.rest.dto.ProjectUpdateDto
import pm.gh.integration.rest.dto.TeamDto
import pm.gh.integration.rest.dto.TeamUpdateDto
import pm.gh.integration.util.toObjectId

object TeamMapper {
    fun Team.toDto(): TeamDto {
        return TeamDto(
            name = name,
            projectManagerId = projectManagerId.toString(),
            teamMemberIds = teamMemberIds?.map { it.toString() },
            projectIds = projectIds?.map { it.toString() }
        )
    }


    fun TeamDto.toModel(): Team {
        return Team(
            name = name,
            projectManagerId = projectManagerId?.toObjectId(),
            teamMemberIds = teamMemberIds?.map { it.toObjectId() },
            projectIds = projectIds?.map { it.toObjectId() },
            id = null
        )
    }

    fun Team.partialUpdate(teamUpdateDto: TeamUpdateDto): Team {
        return copy(
            name = teamUpdateDto.name ?: name,
            projectManagerId = teamUpdateDto.projectManagerId?.toObjectId() ?: projectManagerId,
        )
    }
}