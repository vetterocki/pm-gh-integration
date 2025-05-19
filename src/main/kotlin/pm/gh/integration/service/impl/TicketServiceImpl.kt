package pm.gh.integration.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.model.Ticket
import pm.gh.integration.repository.TicketRepository
import pm.gh.integration.rest.dto.TicketUpdateDto
import pm.gh.integration.rest.mapper.ProjectMapper.partialUpdate
import pm.gh.integration.rest.mapper.TicketMapper.partialUpdate
import pm.gh.integration.service.TicketService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TicketServiceImpl(private val ticketRepository: TicketRepository) : TicketService {
    override fun create(ticket: Ticket): Mono<Ticket> {
        return ticketRepository.create(ticket)
    }

    override fun findById(ticketId: String): Mono<Ticket> {
        return ticketRepository.findById(ticketId)
    }

    override fun deleteById(id: String): Mono<Unit> {
        return ticketRepository.deleteById(id)
    }

    override fun getById(id: String): Mono<Ticket> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Ticket not found by id $id") } }
    }

    override fun update(id: String, ticketUpdateDto: TicketUpdateDto): Mono<Ticket> {
        return getById(id)
            .map { it.partialUpdate(ticketUpdateDto) }
            .flatMap { ticketRepository.update(it) }
    }
}