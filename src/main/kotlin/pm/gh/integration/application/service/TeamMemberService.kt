package pm.gh.integration.application.service

import pm.gh.integration.domain.Actor
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.rest.dto.TeamMemberUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TeamMemberService {
    fun create(teamMember: TeamMember, teamId: String): Mono<TeamMember>
    fun update(id: String, teamMemberUpdateDto: TeamMemberUpdateDto): Mono<TeamMember>
    fun deleteById(teamMemberId: String): Mono<Unit>
    fun findById(teamId: String): Mono<TeamMember>
    fun getById(id: String): Mono<TeamMember>
    fun findByGithubCredentials(actor: Actor): Mono<TeamMember>
    fun findByNameOrEmail(credential: String): Mono<TeamMember>
    fun findAllByIdIn(ticketIds: List<String>): Flux<TeamMember>
    fun findAll(): Flux<TeamMember>
    fun save(teamMember: TeamMember): Mono<TeamMember>
}