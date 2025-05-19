package pm.gh.integration.repository

import pm.gh.integration.model.Team
import reactor.core.publisher.Mono

interface TeamRepository {
    fun create(team: Team): Mono<Team>
    fun update(team: Team): Mono<Team>
    fun deleteById(teamId: String): Mono<Unit>
    fun findById(teamId: String): Mono<Team>
}