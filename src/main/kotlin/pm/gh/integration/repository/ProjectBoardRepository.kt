package pm.gh.integration.repository

import pm.gh.integration.model.ProjectBoard
import reactor.core.publisher.Mono

interface ProjectBoardRepository {
    fun create(projectBoard: ProjectBoard): Mono<ProjectBoard>
    fun findById(id: String): Mono<ProjectBoard>
    fun deleteById(id: String): Mono<Unit>
    fun update(projectBoard: ProjectBoard): Mono<ProjectBoard>
}