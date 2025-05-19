package pm.gh.integration.service

import pm.gh.integration.model.ProjectLabel
import pm.gh.integration.rest.dto.ProjectLabelDto
import reactor.core.publisher.Mono

interface ProjectLabelService {
    fun create(projectLabel: ProjectLabel): Mono<ProjectLabel>
    fun findByName(labelName: String): Mono<ProjectLabel>
    fun deleteById(id: String): Mono<Unit>
    fun findById(id: String): Mono<ProjectLabel>
    fun update(id: String, projectLabelDto: ProjectLabelDto): Mono<ProjectLabel>
    fun getById(id: String): Mono<ProjectLabel>
}