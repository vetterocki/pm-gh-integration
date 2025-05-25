package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.TeamService
import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.mongo.repository.TeamMemberRepository
import pm.gh.integration.infrastructure.mongo.repository.TeamRepository
import pm.gh.integration.infrastructure.rest.dto.TeamUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMapper.partialUpdate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TeamServiceImpl(
    private val teamRepository: TeamRepository,
    private val teamMemberRepository: TeamMemberRepository,
) : TeamService {
    override fun create(team: Team, projectManagerName: String): Mono<Team> {
        return teamMemberRepository.findByNameOrEmail(projectManagerName)
            .map { team.copy(projectManager = it) }
            .flatMap { teamRepository.create(team) }
    }

    override fun findByName(name: String): Mono<Team> {
        return teamRepository.findByName(name)
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

    override fun findAll(): Flux<Team> {
        return teamRepository.findAll()
    }
}