package pm.gh.integration.repository

import pm.gh.integration.model.TeamMember
import reactor.core.publisher.Mono

interface TeamMemberRepository {
    fun create(teamMember: TeamMember): Mono<TeamMember>
    fun update(teamMember: TeamMember): Mono<TeamMember>
    fun deleteById(teamMemberId: String): Mono<Unit>
    fun findById(teamId: String): Mono<TeamMember>
}