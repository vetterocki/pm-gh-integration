package pm.gh.integration.application.service

import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.rest.dto.TeamUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TeamService {
    fun create(team: Team, projectManagerName: String): Mono<Team>
    fun findByName(name: String): Mono<Team>
    fun update(id: String, teamUpdateDto: TeamUpdateDto): Mono<Team>
    fun deleteById(teamId: String): Mono<Unit>
    fun findById(teamId: String): Mono<Team>
    fun getById(id: String): Mono<Team>
    fun findAll(): Flux<Team>
}