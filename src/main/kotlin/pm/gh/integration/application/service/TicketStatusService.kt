package pm.gh.integration.application.service

import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.rest.dto.TicketStatusDto
import reactor.core.publisher.Mono

interface TicketStatusService {
    fun create(ticketStatus: TicketStatus): Mono<TicketStatus>
    fun findById(ticketId: String): Mono<TicketStatus>
    fun deleteById(id: String): Mono<Unit>
    fun update(id: String, ticketStatusDto: TicketStatusDto): Mono<TicketStatus>
    fun getById(id: String): Mono<TicketStatus>
    fun findByName(name: String): Mono<TicketStatus>
}