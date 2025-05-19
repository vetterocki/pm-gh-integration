package pm.gh.integration.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.model.ProjectBoard
import pm.gh.integration.repository.ProjectBoardRepository
import pm.gh.integration.rest.dto.ProjectBoardDto
import pm.gh.integration.rest.mapper.ProjectBoardMapper.partialUpdate
import pm.gh.integration.service.ProjectBoardService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ProjectBoardServiceImpl(private val projectBoardRepository: ProjectBoardRepository) : ProjectBoardService {
    override fun create(projectBoard: ProjectBoard): Mono<ProjectBoard> {
        return projectBoardRepository.create(projectBoard)
    }

    override fun findById(id: String): Mono<ProjectBoard> {
        return projectBoardRepository.findById(id)
    }

    override fun deleteById(id: String): Mono<Unit> {
        return projectBoardRepository.deleteById(id)
    }

    override fun update(id: String, projectBoardDto: ProjectBoardDto): Mono<ProjectBoard> {
        return getById(id)
            .map { it.partialUpdate(projectBoardDto) }
            .flatMap { projectBoardRepository.update(it) }
    }

    override fun getById(id: String): Mono<ProjectBoard> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Project board not found by id $id") } }
    }
}