package pm.gh.integration.application.service

import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.rest.dto.ProjectUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectService {
    fun create(project: Project, teamName: String, projectOwnerName: String): Mono<Project>
    fun findAllByTeamMemberId(teamMemberId: String): Flux<Project>
    fun findByName(projectName: String): Mono<Project>
    fun deleteById(id: String): Mono<Unit>
    fun update(id: String, projectUpdateDto: ProjectUpdateDto): Mono<Project>
    fun findById(id: String): Mono<Project>
    fun getById(id: String): Mono<Project>
    fun findAll(): Flux<Project>
    fun addLabelToProject(projectLabel: ProjectLabel, projectId: String): Mono<Unit>
    fun removeLabelFromProject(projectLabel: ProjectLabel, projectId: String): Mono<Unit>
}