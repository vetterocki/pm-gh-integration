package pm.gh.integration.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.model.ProjectLabel
import pm.gh.integration.repository.ProjectLabelRepository
import pm.gh.integration.rest.dto.ProjectLabelDto
import pm.gh.integration.rest.mapper.ProjectLabelMapper.partialUpdate
import pm.gh.integration.service.ProjectLabelService
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