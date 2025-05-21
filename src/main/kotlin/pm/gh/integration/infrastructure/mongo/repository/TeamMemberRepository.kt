package pm.gh.integration.infrastructure.mongo.repository

import pm.gh.integration.domain.Actor
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import reactor.core.publisher.Mono

interface TeamMemberRepository {
    fun create(teamMember: TeamMember): Mono<TeamMember>
    fun update(teamMember: TeamMember): Mono<TeamMember>
    fun deleteById(teamMemberId: String): Mono<Unit>
    fun findById(teamId: String): Mono<TeamMember>
    fun findByGithubCredentials(actor: Actor): Mono<TeamMember>
}