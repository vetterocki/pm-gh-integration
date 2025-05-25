package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.application.service.TeamService
import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.mongo.repository.ProjectRepository
import pm.gh.integration.infrastructure.rest.dto.ProjectUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectMapper.partialUpdate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val teamService: TeamService,
    private val teamMemberService: TeamMemberService,
) : ProjectService {
    override fun create(project: Project, teamName: String, projectOwnerName: String): Mono<Project> {
        return Mono.zip(teamService.findByName(teamName), teamMemberService.findByNameOrEmail(projectOwnerName))
            .map { (team, projectManager) -> project.copy(team = team, projectOwner = projectManager) }
            .flatMap { projectRepository.create(it) }
    }

    override fun findAllByTeamMemberId(teamMemberId: String): Flux<Project> {
        return projectRepository.findAllByTeamMemberId(teamMemberId)
    }

    override fun findByName(projectName: String): Mono<Project> {
        return projectRepository.findByName(projectName)
    }

    override fun deleteById(id: String): Mono<Unit> {
        return projectRepository.deleteById(id)
    }

    override fun update(id: String, projectUpdateDto: ProjectUpdateDto): Mono<Project> {
        return getById(id)
            .map { it.partialUpdate(projectUpdateDto) }
            .flatMap { projectRepository.update(it) }
    }

    override fun findById(id: String): Mono<Project> {
        return projectRepository.findById(id)
    }

    override fun getById(id: String): Mono<Project> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Project not found by id $id") } }
    }

    override fun findAll(): Flux<Project> {
        return projectRepository.findAll()
    }
}