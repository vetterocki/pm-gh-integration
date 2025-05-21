package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.mongo.repository.ProjectLabelRepository
import reactor.core.publisher.Mono

@Repository
class ProjectLabelRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : ProjectLabelRepository {
    override fun create(projectLabel: ProjectLabel): Mono<ProjectLabel> {
        return mongoTemplate.insert(projectLabel)
    }

    override fun findById(id: String): Mono<ProjectLabel> {
        return mongoTemplate.findById<ProjectLabel>(query(getFindByIdCriteria(id)))
    }

    override fun findByName(labelName: String): Mono<ProjectLabel> {
        return mongoTemplate.findOne(query(where(ProjectLabel::name.name).isEqualTo(labelName)))
    }

    override fun deleteById(id: String): Mono<Unit> {
        return mongoTemplate.remove(query(getFindByIdCriteria(id))).thenReturn(Unit)
    }

    private fun getFindByIdCriteria(id: String): CriteriaDefinition {
        return where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId())
    }

    override fun update(updatedProjectLabel: ProjectLabel): Mono<ProjectLabel> {
        return mongoTemplate.save(updatedProjectLabel)
    }
}