package pm.gh.integration.infrastructure.mongo.repository

import pm.gh.integration.infrastructure.mongo.model.ProjectBoard
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectBoardRepository {
    fun create(projectBoard: ProjectBoard): Mono<ProjectBoard>
    fun findById(id: String): Mono<ProjectBoard>
    fun deleteById(id: String): Mono<Unit>
    fun update(projectBoard: ProjectBoard): Mono<ProjectBoard>
    fun findAllByProjectId(projectId: String): Flux<ProjectBoard>
}