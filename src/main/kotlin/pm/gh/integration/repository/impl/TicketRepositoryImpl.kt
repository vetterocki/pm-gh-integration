package pm.gh.integration.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.model.Ticket
import pm.gh.integration.repository.TicketRepository
import pm.gh.integration.util.toObjectId
import reactor.core.publisher.Mono

@Repository
class TicketRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : TicketRepository {
    override fun create(ticket: Ticket): Mono<Ticket> {
        return mongoTemplate.insert(ticket)
    }

    override fun findById(ticketId: String): Mono<Ticket> {
        return mongoTemplate.findById(ticketId.toObjectId())
    }

    override fun deleteById(id: String): Mono<Unit> {
        return mongoTemplate.remove(query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId())))
            .thenReturn(Unit)
    }

    override fun update(ticket: Ticket): Mono<Ticket> {
        return mongoTemplate.save(ticket)
    }
}