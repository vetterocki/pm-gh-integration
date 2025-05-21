package pm.gh.integration.application.service

import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto
import reactor.core.publisher.Mono

interface ProjectLabelService {
    fun create(projectLabel: ProjectLabel): Mono<ProjectLabel>
    fun findByName(labelName: String): Mono<ProjectLabel>
    fun deleteById(id: String): Mono<Unit>
    fun findById(id: String): Mono<ProjectLabel>
    fun update(id: String, projectLabelDto: ProjectLabelDto): Mono<ProjectLabel>
    fun getById(id: String): Mono<ProjectLabel>
}