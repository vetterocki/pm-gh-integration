package pm.gh.integration.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.model.TicketStatus
import pm.gh.integration.repository.TicketStatusRepository
import pm.gh.integration.rest.dto.TicketStatusDto
import pm.gh.integration.rest.mapper.ProjectMapper.partialUpdate
import pm.gh.integration.rest.mapper.TicketStatusMapper.partialUpdate
import pm.gh.integration.service.TicketStatusService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TicketStatusServiceImpl(private val ticketStatusRepository: TicketStatusRepository) : TicketStatusService {
    override fun create(ticketStatus: TicketStatus): Mono<TicketStatus> {
        return ticketStatusRepository.create(ticketStatus)
    }

    override fun findById(ticketId: String): Mono<TicketStatus> {
        return ticketStatusRepository.findById(ticketId)
    }

    override fun deleteById(id: String): Mono<Unit> {
        return ticketStatusRepository.deleteById(id)
    }

    override fun getById(id: String): Mono<TicketStatus> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Ticket status not found by id $id") } }
    }

    override fun update(id: String, ticketStatusDto: TicketStatusDto): Mono<TicketStatus> {
        return getById(id)
            .map { it.partialUpdate(ticketStatusDto) }
            .flatMap { ticketStatusRepository.update(it) }
    }
}