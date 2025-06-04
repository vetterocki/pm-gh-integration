package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.ProjectBoardService
import pm.gh.integration.application.service.ProjectLabelService
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.application.service.TicketStatusService
import pm.gh.integration.application.util.toObjectId
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
    private val ticketStatusService: TicketStatusService,
    private val projectBoardService: ProjectBoardService,
    private val labelService: ProjectLabelService
) : TicketService {

    override fun create(ticket: Ticket, projectId: String, projectBoardId: String, reporterId: String, labelIds: List<String>): Mono<Ticket> {
        return Mono.zip(
            projectService.getById(projectId),
            teamMemberService.getById(reporterId),
            projectBoardService.getById(projectBoardId)
        )
            .flatMap { (project, reporter, projectBoard) ->
                documentSequenceHolder.acquireSequenceCounter(SEQUENCE_NAME)
                    .map {
                        ticket.copy(
                            projectId = project.id,
                            reporter = reporter,
                            projectBoardId = projectBoard.id,
                            ticketIdentifier = "${project.key}-${it}"
                        )
                    }
            }
            .flatMap { ticket ->
                labelService.findAllByIdIn(labelIds.map { it.toObjectId() })
                    .collectList()
                    .map { ticket.copy(labels = it) }
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

    override fun findAllByTicketIdentifierContaining(ticketIdentifier: String): Flux<Ticket> {
        return ticketRepository.findAllByTicketIdentifierContaining(ticketIdentifier)
    }

    override fun findAllByProjectBoardId(projectBoardId: String): Flux<Ticket> {
        return ticketRepository.findAllByProjectBoardId(projectBoardId)
    }

    override fun findAllByProjectId(projectId: String): Flux<Ticket> {
        return ticketRepository.findAllByProjectId(projectId)
    }

    override fun findAllByIdIn(ticketId: String): Flux<Ticket> {
        return findById(ticketId)
            .map { it.linkedTicketIds?.map { it.toString() }.orEmpty() }
            .flatMapMany { ticketRepository.findAllByIdIn(it) }
    }

    override fun findAllByProjectBoardIdGroupedByStatus(projectBoardId: String): Mono<Map<String, Flux<Ticket>>> {
        return ticketRepository.findAllByProjectBoardIdGroupedByStatus(projectBoardId)
    }

    override fun updateTicketStatus(ticketIdentifier: String, status: String): Mono<Ticket> {
        return ticketStatusService.findByName(status)
            .flatMap { ticketRepository.updateTicketStatus(ticketIdentifier, it) }
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
            .flatMap { ticketRepository.updateTicketReviewers(ticketIdentifier, it) }

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

    override fun assignTicket(
        ticketId: String,
        memberName: String,
    ): Mono<Ticket> {
        return Mono.zip(
            findById(ticketId),
            teamMemberService.findByNameOrEmail(memberName)
        )
            .map { (ticket, member) -> ticket.copy(assignee = member) }
            .flatMap { ticketRepository.save(it) }
    }

    override fun unassignTicket(
        ticketId: String,
    ): Mono<Unit> {
        return findById(ticketId)
            .map { it.copy(assignee = null) }
            .flatMap { ticketRepository.save(it) }
            .thenReturn(Unit)
    }


    override fun update(id: String, ticketUpdateDto: TicketUpdateDto): Mono<Ticket> {
        return getById(id)
            .map { it.partialUpdate(ticketUpdateDto) }
            .flatMap { ticketRepository.update(it) }
    }
}