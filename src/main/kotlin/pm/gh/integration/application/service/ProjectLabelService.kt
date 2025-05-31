package pm.gh.integration.application.service

import org.bson.types.ObjectId
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectLabelService {
    fun create(projectLabel: ProjectLabel, projectId: String): Mono<ProjectLabel>
    fun findByName(labelName: String): Mono<ProjectLabel>
    fun deleteById(id: String): Mono<Unit>
    fun findById(id: String): Mono<ProjectLabel>
    fun update(id: String, projectLabelDto: ProjectLabelDto): Mono<ProjectLabel>
    fun getById(id: String): Mono<ProjectLabel>
    fun findAllByProjectId(projectId: String): Flux<ProjectLabel>
    fun findAll(): Flux<ProjectLabel>
    fun findAllByIdIn(projectLabelIds: List<ObjectId>): Flux<ProjectLabel>
}