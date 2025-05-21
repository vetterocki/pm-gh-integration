package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.ProjectLabelService
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.mongo.repository.ProjectLabelRepository
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.partialUpdate
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ProjectLabelServiceImpl(private val projectLabelRepository: ProjectLabelRepository) : ProjectLabelService {
    override fun create(projectLabel: ProjectLabel): Mono<ProjectLabel> {
        return projectLabelRepository.create(projectLabel)
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
}