package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.rest.dto.TeamMemberDto
import pm.gh.integration.infrastructure.rest.dto.TeamMemberUpdateDto

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
            position = position,
            fullName = firstName + " " + lastName,
            loginInGithub = TODO()
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