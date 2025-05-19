package pm.gh.integration.service

import pm.gh.integration.model.Ticket
import pm.gh.integration.rest.dto.TicketUpdateDto
import reactor.core.publisher.Mono

interface TicketService {
    fun create(ticket: Ticket): Mono<Ticket>
    fun findById(ticketId: String): Mono<Ticket>
    fun deleteById(id: String): Mono<Unit>
    fun update(id: String, ticketUpdateDto: TicketUpdateDto): Mono<Ticket>
    fun getById(id: String): Mono<Ticket>
}