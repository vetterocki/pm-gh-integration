package pm.gh.integration.service

import pm.gh.integration.model.TeamMember
import pm.gh.integration.rest.dto.TeamMemberUpdateDto
import reactor.core.publisher.Mono

interface TeamMemberService {
    fun create(teamMember: TeamMember): Mono<TeamMember>
    fun update(id: String, teamMemberUpdateDto: TeamMemberUpdateDto): Mono<TeamMember>
    fun deleteById(teamMemberId: String): Mono<Unit>
    fun findById(teamId: String): Mono<TeamMember>
    fun getById(id: String): Mono<TeamMember>
}