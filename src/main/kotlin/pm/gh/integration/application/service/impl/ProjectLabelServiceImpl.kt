package pm.gh.integration.application.service.impl

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import pm.gh.integration.application.service.ProjectLabelService
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.mongo.repository.ProjectLabelRepository
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.partialUpdate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ProjectLabelServiceImpl(
    private val projectLabelRepository: ProjectLabelRepository,
    private val projectService: ProjectService,
) : ProjectLabelService {
    override fun create(projectLabel: ProjectLabel, projectId: String): Mono<ProjectLabel> {
        return projectService.findById(projectId)
            .map { projectLabel.copy(projectId = it.id) }
            .flatMap { projectLabelRepository.create(it) }
            .flatMap { projectService.addLabelToProject(it, projectId).thenReturn(it) }
    }

    override fun findByName(labelName: String): Mono<ProjectLabel> {
        return projectLabelRepository.findByName(labelName)
    }

    override fun deleteById(id: String): Mono<Unit> {
        return projectLabelRepository.deleteById(id)
    }

    override fun update(id: String, projectLabelDto: ProjectLabelDto): Mono<ProjectLabel> {
        return getById(id)
            .map { it.partialUpdate(projectLabelDto) }
            .flatMap { projectLabelRepository.update(it) }
    }

    override fun findById(id: String): Mono<ProjectLabel> {
        return projectLabelRepository.findById(id)
    }

    override fun getById(id: String): Mono<ProjectLabel> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Project label not found by id $id") } }
    }

    override fun findAllByProjectId(projectId: String): Flux<ProjectLabel> {
        return projectService.findById(projectId)
            .mapNotNull { it.projectLabelIds }
            .flatMapMany { projectLabelRepository.findAllByIdIn(it.orEmpty()) }
    }

    override fun findAll(): Flux<ProjectLabel> {
        return projectLabelRepository.findAll()
    }

    override fun findAllByIdIn(projectLabelIds: List<ObjectId>): Flux<ProjectLabel> {
        return projectLabelRepository.findAllByIdIn(projectLabelIds)
    }
}