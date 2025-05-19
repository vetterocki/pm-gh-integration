package pm.gh.integration.rest.mapper

import pm.gh.integration.model.TeamMember
import pm.gh.integration.rest.dto.TeamMemberDto
import pm.gh.integration.rest.dto.TeamMemberUpdateDto
import pm.gh.integration.util.toObjectId

object TeamMemberMapper {
    fun TeamMember.toDto(): TeamMemberDto {
        return TeamMemberDto(
            firstName = firstName,
            lastName = lastName,
            email = email,
            teamId = teamId.toString(),
            position = position
        )
    }

    fun TeamMemberDto.toModel(): TeamMember {
        return TeamMember(
            id = null,
            firstName = firstName,
            lastName = lastName,
            email = email,
            teamId = teamId.toObjectId(),
            position = position
        )
    }

    fun TeamMember.partialUpdate(dto: TeamMemberUpdateDto): TeamMember {
        return copy(
            firstName = dto.firstName ?: firstName,
            lastName = dto.lastName ?: lastName,
            email = dto.email ?: email,
            teamId = dto.teamId?.toObjectId() ?: teamId,
            position = dto.position ?: position
        )
    }
}