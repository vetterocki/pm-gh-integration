package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.TeamService
import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.mongo.repository.TeamRepository
import pm.gh.integration.infrastructure.rest.dto.TeamUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMapper.partialUpdate
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