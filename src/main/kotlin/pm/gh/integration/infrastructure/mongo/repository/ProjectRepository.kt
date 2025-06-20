package pm.gh.integration.infrastructure.mongo.repository

import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectRepository {
    fun findAll(): Flux<Project>
    fun create(project: Project): Mono<Project>
    fun findAllByTeamMemberId(teamMemberId: String): Flux<Project>
    fun findByName(projectName: String): Mono<Project>
    fun deleteById(id: String): Mono<Unit>
    fun update(updatedProject: Project): Mono<Project>
    fun findById(id: String): Mono<Project>
    fun addLabelToProject(projectLabel: ProjectLabel, projectId: String): Mono<Unit>
    fun removeLabelFromProject(projectLabel: ProjectLabel, projectId: String): Mono<Unit>
}