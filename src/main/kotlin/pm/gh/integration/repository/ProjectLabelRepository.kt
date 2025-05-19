package pm.gh.integration.repository

import pm.gh.integration.model.ProjectLabel
import reactor.core.publisher.Mono

interface ProjectLabelRepository {
    fun create(projectLabel: ProjectLabel): Mono<ProjectLabel>
    fun findById(id: String): Mono<ProjectLabel>
    fun findByName(labelName: String): Mono<ProjectLabel>
    fun deleteById(id: String): Mono<Unit>
    fun update(updatedProjectLabel: ProjectLabel): Mono<ProjectLabel>
}