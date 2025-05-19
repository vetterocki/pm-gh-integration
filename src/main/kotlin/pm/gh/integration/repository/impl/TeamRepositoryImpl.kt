package pm.gh.integration.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.model.Team
import pm.gh.integration.repository.TeamRepository
import pm.gh.integration.util.toObjectId
import reactor.core.publisher.Mono

@Repository
class TeamRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : TeamRepository {
    override fun create(team: Team): Mono<Team> {
        return mongoTemplate.insert(team)
    }

    override fun update(team: Team): Mono<Team> {
        return mongoTemplate.save(team)
    }

    override fun deleteById(teamId: String): Mono<Unit> {
        return mongoTemplate.remove(qetCriteriaById(teamId)).thenReturn(Unit)
    }

    override fun findById(teamId: String): Mono<Team> {
        return mongoTemplate.findById(teamId.toObjectId())
    }

    private fun qetCriteriaById(id: String): Query {
        return query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId()))
    }
}