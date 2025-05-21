package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.mongo.repository.ProjectRepository
import pm.gh.integration.infrastructure.rest.dto.ProjectUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectMapper.partialUpdate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ProjectServiceImpl(private val projectRepository: ProjectRepository) : ProjectService {
    override fun create(project: Project): Mono<Project> {
        return projectRepository.create(project)
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
}