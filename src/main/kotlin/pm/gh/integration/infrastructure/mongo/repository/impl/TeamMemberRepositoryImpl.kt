package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.domain.Actor
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.mongo.repository.TeamMemberRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TeamMemberRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : TeamMemberRepository {
    override fun create(teamMember: TeamMember): Mono<TeamMember> {
        return mongoTemplate.insert(teamMember)
    }

    override fun update(teamMember: TeamMember): Mono<TeamMember> {
        return mongoTemplate.save(teamMember)
    }

    override fun deleteById(teamMemberId: String): Mono<Unit> {
        return mongoTemplate.remove<TeamMember>(
            query(where(Fields.UNDERSCORE_ID).isEqualTo(teamMemberId.toObjectId()))
        ).thenReturn(Unit)
    }

    override fun findById(teamId: String): Mono<TeamMember> {
        return mongoTemplate.findById(teamId.toObjectId())
    }

    override fun findByGithubCredentials(actor: Actor): Mono<TeamMember> {
        return mongoTemplate.findOne<TeamMember>(
            query(
                Criteria().orOperator(
                    where(TeamMember::email.name).isEqualTo(actor.email),
                    where(TeamMember::loginInGithub.name).isEqualTo(actor.login),
                    where(TeamMember::fullName.name).isEqualTo(actor.name)
                )
            )
        )
    }

    override fun findByNameOrEmail(credential: String): Mono<TeamMember> {
        return mongoTemplate.findOne<TeamMember>(
            query(
                Criteria().orOperator(
                    where(TeamMember::email.name).isEqualTo(credential),
                    where(TeamMember::fullName.name).isEqualTo(credential),
                )
            )
        )
    }

    override fun findAllByTeamId(teamId: String): Flux<TeamMember> {
        return mongoTemplate.find<TeamMember>(
            query(
                where(
                    TeamMember::teamId.name
                ).isEqualTo(teamId.toObjectId())
            )
        )
    }

    override fun findAllByIdIn(ticketIds: List<String>): Flux<TeamMember> {
        return mongoTemplate.find<TeamMember>(query(where(Fields.UNDERSCORE_ID).`in`(ticketIds.map { it.toObjectId() })))
    }

    override fun findAll(): Flux<TeamMember> {
        return mongoTemplate.findAll<TeamMember>()
    }

    override fun save(teamMember: TeamMember): Mono<TeamMember> {
        return mongoTemplate.save(teamMember)
    }

    override fun findByEmail(email: String): Mono<TeamMember> {
        return mongoTemplate.findOne<TeamMember>(
            query(
                where(TeamMember::email.name).isEqualTo(email)
            )
        )
    }
}