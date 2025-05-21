package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.TicketStatusService
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.mongo.repository.TicketStatusRepository
import pm.gh.integration.infrastructure.rest.dto.TicketStatusDto
import pm.gh.integration.infrastructure.rest.mapper.TicketStatusMapper.partialUpdate
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