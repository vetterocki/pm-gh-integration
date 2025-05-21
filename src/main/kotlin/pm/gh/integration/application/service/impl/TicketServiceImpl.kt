package pm.gh.integration.application.service.impl

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.domain.Actor
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.model.Ticket.Companion.SEQUENCE_NAME
import pm.gh.integration.infrastructure.mongo.repository.TicketRepository
import pm.gh.integration.infrastructure.mongo.repository.impl.DocumentSequenceHolder
import pm.gh.integration.infrastructure.rest.dto.TicketUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TicketMapper.partialUpdate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
    private val projectService: ProjectService,
    private val teamMemberService: TeamMemberService,
    private val documentSequenceHolder: DocumentSequenceHolder,
) : TicketService {

    override fun create(ticket: Ticket, projectId: String, reporterId: String, assigneeId: String): Mono<Ticket> {
        return Mono.zip(
            projectService.getById(projectId),
            teamMemberService.getById(reporterId),
            teamMemberService.getById(assigneeId)
        )
            .flatMap { (project, reporter, assignee) ->
                documentSequenceHolder.acquireSequenceCounter(SEQUENCE_NAME)
                    .map {
                        ticket.copy(
                            projectId = project.id,
                            reporterId = reporter.id,
                            assigneeId = assignee.id,
                            ticketIdentifier = "${project.key}-${it}"
                        )
                    }
            }
            .flatMap { ticketRepository.create(it) }
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

    override fun findByTicketIdentifier(ticketIdentifier: String): Mono<Ticket> {
        return ticketRepository.findByTicketIdentifier(ticketIdentifier)
    }

    override fun updateTicketStatus(ticketIdentifier: String, status: String): Mono<Ticket> {
        return ticketRepository.updateTicketStatus(ticketIdentifier, status)
    }

    override fun updateTicketGithubDescription(
        ticketIdentifier: String,
        githubDescription: String,
    ): Mono<Ticket> {
        return ticketRepository.updateTicketGithubDescription(ticketIdentifier, githubDescription)
    }

    override fun updateTicketReviewers(
        ticketIdentifier: String,
        reviewers: List<Actor>,
    ): Mono<Ticket> {
        return Flux.fromIterable(reviewers)
            .flatMap { teamMemberService.findByGithubCredentials(it) }
            .mapNotNull { it.id }
            .collectList()
            .flatMap { ticketRepository.updateTicketReviewers(ticketIdentifier, it)}

    }

    override fun updateTicketPullRequests(
        ticketIdentifier: String,
        pullRequest: PullRequest,
    ): Mono<Ticket> {
        return findByTicketIdentifier(ticketIdentifier)
            .map { it.linkedPullRequests.orEmpty() }
            .map { currentPullRequestsInTicket ->
                currentPullRequestsInTicket
                    .filter { it.repositoryName == pullRequest.repositoryName }
                    .plus(pullRequest)
            }
            .flatMap { ticketRepository.updateTicketPullRequests(ticketIdentifier, pullRequest) }
    }

    override fun updateTicketWorkflowRuns(
        ticketIdentifier: String,
        workflowRun: WorkflowRun,
    ): Mono<Ticket> {
        return findByTicketIdentifier(ticketIdentifier)
            .map { it.linkedWorkflowRuns.orEmpty() }
            .map { currentWorkflowRunsInTicket ->
                currentWorkflowRunsInTicket
                    .filter { it.repositoryName == workflowRun.repositoryName }
                    .plus(workflowRun)
            }
            .flatMap { ticketRepository.updateTicketWorkflowRuns(ticketIdentifier, workflowRun) }
    }

    override fun update(id: String, ticketUpdateDto: TicketUpdateDto): Mono<Ticket> {
        return getById(id)
            .map { it.partialUpdate(ticketUpdateDto) }
            .flatMap { ticketRepository.update(it) }
    }
}