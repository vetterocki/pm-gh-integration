package pm.gh.integration.infrastructure.mongo.repository

import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import reactor.core.publisher.Mono

interface TicketStatusRepository {
    fun create(ticketStatus: TicketStatus): Mono<TicketStatus>
    fun findById(ticketId: String): Mono<TicketStatus>
    fun deleteById(id: String): Mono<Unit>
    fun update(ticketStatus: TicketStatus): Mono<TicketStatus>
}