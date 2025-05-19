package pm.gh.integration.repository

import pm.gh.integration.model.Ticket
import reactor.core.publisher.Mono

interface TicketRepository {
    fun create(ticket: Ticket): Mono<Ticket>
    fun findById(ticketId: String): Mono<Ticket>
    fun deleteById(id: String): Mono<Unit>
    fun update(ticket: Ticket): Mono<Ticket>
}