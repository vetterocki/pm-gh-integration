package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.ProjectBoard
import pm.gh.integration.infrastructure.mongo.repository.ProjectBoardRepository
import reactor.core.publisher.Mono

@Repository
class ProjectBoardRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : ProjectBoardRepository {
    override fun create(projectBoard: ProjectBoard): Mono<ProjectBoard> {
        return mongoTemplate.insert(projectBoard)
    }

    override fun findById(id: String): Mono<ProjectBoard> {
        return mongoTemplate.findById(id.toObjectId())
    }

    override fun deleteById(id: String): Mono<Unit> {
        return mongoTemplate.remove(query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId())))
            .thenReturn(Unit)
    }

    override fun update(projectBoard: ProjectBoard): Mono<ProjectBoard> {
        return mongoTemplate.save(projectBoard)
    }
}