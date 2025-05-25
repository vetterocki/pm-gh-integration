package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.model.Ticket.TicketPriority
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.rest.dto.TicketDto
import pm.gh.integration.infrastructure.rest.dto.TicketUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.toModel
import java.time.Instant

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
            status = status.name,
            id = id.toString(),
            projectBoardId = projectBoardId.toString(),
            labels = labels?.map { it.toDto() },
        )
    }

    fun TicketDto.toModel(): Ticket {
        return Ticket(
            id = null,
            projectId = projectId.toObjectId(),
            projectBoardId = projectBoardId.toObjectId(),
            summary = summary,
            description = description,
            reporterId = reporterId.toObjectId(),
            assigneeId = assigneeId.toObjectId(),
            linkedTicketIds = linkedTicketIds?.map { it.toObjectId() },
            priority = TicketPriority.valueOf(priority),
            status = TicketStatus(
                id = null,
                name = status
            ),
            ticketIdentifier = null,
            linkedPullRequests = null,
            linkedWorkflowRuns = null,
            reviewerIds = null,
            githubDescription = null,
            createdAt = Instant.now(),
            labels = labels?.map { it.toModel() }
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