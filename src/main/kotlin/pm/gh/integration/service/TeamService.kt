package pm.gh.integration.service

import pm.gh.integration.model.Team
import pm.gh.integration.rest.dto.TeamUpdateDto
import reactor.core.publisher.Mono

interface TeamService {
    fun create(team: Team): Mono<Team>
    fun update(id: String, teamUpdateDto: TeamUpdateDto): Mono<Team>
    fun deleteById(teamId: String): Mono<Unit>
    fun findById(teamId: String): Mono<Team>
    fun getById(id: String): Mono<Team>
}