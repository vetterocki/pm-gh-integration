package pm.gh.integration.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.model.Team
import pm.gh.integration.repository.TeamRepository
import pm.gh.integration.rest.dto.TeamUpdateDto
import pm.gh.integration.rest.mapper.ProjectMapper.partialUpdate
import pm.gh.integration.rest.mapper.TeamMapper.partialUpdate
import pm.gh.integration.service.TeamService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TeamServiceImpl(private val teamRepository: TeamRepository) : TeamService {
    override fun create(team: Team): Mono<Team> {
        return teamRepository.create(team)
    }

    override fun update(id: String, teamUpdateDto: TeamUpdateDto): Mono<Team> {
        return getById(id)
            .map { it.partialUpdate(teamUpdateDto) }
            .flatMap { teamRepository.update(it) }
    }

    override fun deleteById(teamId: String): Mono<Unit> {
        return teamRepository.deleteById(teamId)
    }

    override fun findById(teamId: String): Mono<Team> {
        return teamRepository.findById(teamId)
    }

    override fun getById(id: String): Mono<Team> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Team not found by id $id") } }
    }
}