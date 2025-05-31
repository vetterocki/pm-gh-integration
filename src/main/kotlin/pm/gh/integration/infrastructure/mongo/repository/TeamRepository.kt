package pm.gh.integration.infrastructure.mongo.repository

import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TeamRepository {
    fun create(team: Team): Mono<Team>
    fun update(team: Team): Mono<Team>
    fun deleteById(teamId: String): Mono<Unit>
    fun findById(teamId: String): Mono<Team>
    fun findByName(name: String): Mono<Team>
    fun findAll(): Flux<Team>
    fun save(team: Team): Mono<Team>
    fun addMember(teamId: String, teamMember: TeamMember): Mono<Unit>
    fun removeMember(teamId: String, teamMember: TeamMember): Mono<Unit>
}