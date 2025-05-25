package pm.gh.integration.application.service

import pm.gh.integration.infrastructure.mongo.model.ProjectBoard
import pm.gh.integration.infrastructure.rest.dto.ProjectBoardDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectBoardService {
    fun create(projectBoard: ProjectBoard): Mono<ProjectBoard>
    fun findById(id: String): Mono<ProjectBoard>
    fun deleteById(id: String): Mono<Unit>
    fun update(id: String, projectBoardDto: ProjectBoardDto): Mono<ProjectBoard>
    fun getById(id: String): Mono<ProjectBoard>
    fun findAllByProjectId(projectId: String): Flux<ProjectBoard>
}