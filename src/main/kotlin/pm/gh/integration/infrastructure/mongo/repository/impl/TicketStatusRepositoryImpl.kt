package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.mongo.repository.TicketStatusRepository
import reactor.core.publisher.Mono

@Repository
class TicketStatusRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : TicketStatusRepository {
    override fun create(ticketStatus: TicketStatus): Mono<TicketStatus> {
        return mongoTemplate.insert(ticketStatus)
    }

    override fun findById(ticketId: String): Mono<TicketStatus> {
        return mongoTemplate.findById(ticketId.toObjectId())
    }

    override fun deleteById(id: String): Mono<Unit> {
        return mongoTemplate.remove(query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId())))
            .thenReturn(Unit)
    }

    override fun update(ticketStatus: TicketStatus): Mono<TicketStatus> {
        return mongoTemplate.save(ticketStatus)
    }

    override fun findByName(name: String): Mono<TicketStatus> {
        return mongoTemplate.findOne<TicketStatus>(query(where(TicketStatus::name.name).isEqualTo(name)))
    }

}