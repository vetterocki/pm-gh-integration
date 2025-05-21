package pm.gh.integration.application.service

import pm.gh.integration.domain.Actor
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.rest.dto.TeamMemberUpdateDto
import reactor.core.publisher.Mono

interface TeamMemberService {
    fun create(teamMember: TeamMember): Mono<TeamMember>
    fun update(id: String, teamMemberUpdateDto: TeamMemberUpdateDto): Mono<TeamMember>
    fun deleteById(teamMemberId: String): Mono<Unit>
    fun findById(teamId: String): Mono<TeamMember>
    fun getById(id: String): Mono<TeamMember>
    fun findByGithubCredentials(actor: Actor): Mono<TeamMember>
}