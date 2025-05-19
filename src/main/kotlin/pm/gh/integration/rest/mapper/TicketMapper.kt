package pm.gh.integration.rest.mapper

import pm.gh.integration.model.Ticket
import pm.gh.integration.model.Ticket.TicketPriority
import pm.gh.integration.model.TicketStatus
import pm.gh.integration.rest.dto.TicketDto
import pm.gh.integration.rest.dto.TicketUpdateDto
import pm.gh.integration.util.toObjectId

object TicketMapper {
    fun Ticket.toDto(): TicketDto {
        return TicketDto(
            projectId = projectId.toString(),
            summary = summary,
            description = description,
            reporterId = reporterId.toString(),
            assigneeId = assigneeId.toString(),
            linkedTicketIds = linkedTicketIds?.map { it.toString() },
            priority = priority.name,
            status = status.name
        )
    }

    fun TicketDto.toModel(): Ticket {
        return Ticket(
            id = null,
            projectId = projectId.toObjectId(),
            summary = summary,
            description = description,
            reporterId = reporterId.toObjectId(),
            assigneeId = assigneeId.toObjectId(),
            linkedTicketIds = linkedTicketIds?.map { it.toObjectId() },
            priority = TicketPriority.valueOf(priority),
            status = TicketStatus(
                id = null,
                name = status
            )
        )
    }

    fun Ticket.partialUpdate(ticketUpdateDto: TicketUpdateDto): Ticket {
        return copy(
            projectId = ticketUpdateDto.projectId?.toObjectId() ?: projectId,
            summary = ticketUpdateDto.summary ?: summary,
            description = ticketUpdateDto.description ?: description,
            assigneeId = ticketUpdateDto.assigneeId?.toObjectId() ?: assigneeId,
            linkedTicketIds = ticketUpdateDto.linkedTicketIds?.map { it.toObjectId() } ?: linkedTicketIds,
            priority = ticketUpdateDto.priority?.let { TicketPriority.valueOf(it) } ?: priority,
            status = ticketUpdateDto.status?.let {
                TicketStatus(
                    id = null,
                    name = it
                )
            } ?: status
        )
    }
}