package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findAndModify
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.repository.TeamRepository
import reactor.core.publisher.Flux
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
        return mongoTemplate.remove<Team>(qetCriteriaById(teamId)).thenReturn(Unit)
    }

    override fun findById(teamId: String): Mono<Team> {
        return mongoTemplate.findById(teamId.toObjectId())
    }

    override fun findByName(name: String): Mono<Team> {
        return mongoTemplate.findOne(query(where(Team::name.name).isEqualTo(name)))
    }

    override fun findAll(): Flux<Team> {
        return mongoTemplate.findAll<Team>()
    }

    override fun save(team: Team): Mono<Team> {
        return mongoTemplate.save(team)
    }

    override fun addMember(
        teamId: String,
        teamMember: TeamMember,
    ): Mono<Unit> {
        return mongoTemplate.findAndModify<Team>(
            query(where(Fields.UNDERSCORE_ID).isEqualTo(teamId)),
            Update().push(Team::teamMemberIds.name, teamMember.id),
            FindAndModifyOptions.options().returnNew(true)
        ).thenReturn(Unit)
    }

    override fun removeMember(
        teamId: String,
        teamMember: TeamMember,
    ): Mono<Unit> {
        return mongoTemplate.findAndModify<Team>(
            query(where(Fields.UNDERSCORE_ID).isEqualTo(teamId)),
            Update().pull(Team::teamMemberIds.name, teamMember.id),
            FindAndModifyOptions.options().returnNew(true)
        ).thenReturn(Unit)
    }

    private fun qetCriteriaById(id: String): Query {
        return query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId()))
    }
}