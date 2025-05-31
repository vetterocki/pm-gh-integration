package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.model.Ticket.TicketPriority
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.rest.dto.TicketCreateDto
import pm.gh.integration.infrastructure.rest.dto.TicketDto
import pm.gh.integration.infrastructure.rest.dto.TicketUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.toModel
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.toDto
import java.time.Instant

object TicketMapper {
    fun Ticket.toDto(): TicketDto {
        return TicketDto(
            projectId = projectId.toString(),
            summary = summary,
            description = description,
            linkedTicketIds = linkedTicketIds?.map { it.toString() },
            priority = priority.name,
            status = status.name,
            id = id.toString(),
            projectBoardId = projectBoardId.toString(),
            labels = labels?.map { it.toDto() },
            linkedPullRequests = linkedPullRequests,
            linkedWorkflowRuns = linkedWorkflowRuns,
            githubDescription = githubDescription,
            createdAt = createdAt,
            ticketIdentifier = ticketIdentifier.orEmpty(),
            reporter = reporter?.toDto(),
            assignee = assignee?.toDto(),
            reviewers = reviewerIds?.map { it.toString() },
        )
    }

    fun TicketCreateDto.toModel(): Ticket {
        return Ticket(
            id = null,
            projectId = projectId.toObjectId(),
            projectBoardId = projectBoardId.toObjectId(),
            summary = summary,
            description = description,
            linkedTicketIds = linkedTicketIds?.map { it.toObjectId() },
            priority = TicketPriority.valueOf(priority),
            status = TicketStatus(
                id = null,
                name = "TO_DO"
            ),
            linkedPullRequests = null,
            linkedWorkflowRuns = null,
            reviewerIds = null,
            githubDescription = null,
            createdAt = Instant.now(),
            labels = null,
            reporter = null,
            assignee = null,
            ticketIdentifier = null
        )
    }

    fun Ticket.partialUpdate(ticketUpdateDto: TicketUpdateDto): Ticket {
        return copy(
            projectId = ticketUpdateDto.projectId?.toObjectId() ?: projectId,
            summary = ticketUpdateDto.summary ?: summary,
            description = ticketUpdateDto.description ?: description,
            linkedTicketIds = ticketUpdateDto.linkedTicketIds?.map { it.toObjectId() } ?: linkedTicketIds,
            priority = ticketUpdateDto.priority?.let { TicketPriority.valueOf(it.uppercase()) } ?: priority,
            status = ticketUpdateDto.status?.let {
                TicketStatus(
                    id = null,
                    name = it
                )
            } ?: status
        )
    }
}