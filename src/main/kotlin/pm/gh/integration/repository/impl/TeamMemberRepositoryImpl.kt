package pm.gh.integration.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.model.TeamMember
import pm.gh.integration.repository.TeamMemberRepository
import pm.gh.integration.util.toObjectId
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
        return mongoTemplate.remove(query(where(Fields.UNDERSCORE_ID).isEqualTo(teamMemberId.toObjectId())))
            .thenReturn(Unit)
    }

    override fun findById(teamId: String): Mono<TeamMember> {
        return mongoTemplate.findById(teamId.toObjectId())
    }
}