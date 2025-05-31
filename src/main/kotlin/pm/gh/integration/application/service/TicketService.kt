package pm.gh.integration.application.service

import pm.gh.integration.domain.Actor
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.rest.dto.TicketUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TicketService {
    fun create(ticket: Ticket, projectId: String, projectBoardId: String, reporterId: String, labelsIds: List<String>): Mono<Ticket>
    fun findById(ticketId: String): Mono<Ticket>
    fun deleteById(id: String): Mono<Unit>
    fun update(id: String, ticketUpdateDto: TicketUpdateDto): Mono<Ticket>
    fun getById(id: String): Mono<Ticket>
    fun findByTicketIdentifier(ticketIdentifier: String): Mono<Ticket>
    fun findAllByTicketIdentifierContaining(ticketIdentifier: String): Flux<Ticket>
    fun findAllByProjectBoardId(projectBoardId: String): Flux<Ticket>
    fun findAllByProjectId(projectId: String): Flux<Ticket>
    fun findAllByIdIn(ticketId: String): Flux<Ticket>
    fun findAllByProjectBoardIdGroupedByStatus(projectBoardId: String): Mono<Map<String, Flux<Ticket>>>
    fun updateTicketStatus(ticketIdentifier: String, status: String): Mono<Ticket>
    fun updateTicketGithubDescription(ticketIdentifier: String, githubDescription: String): Mono<Ticket>
    fun updateTicketReviewers(ticketIdentifier: String, reviewers: List<Actor>): Mono<Ticket>
    fun updateTicketPullRequests(ticketIdentifier: String, pullRequest: PullRequest): Mono<Ticket>
    fun updateTicketWorkflowRuns(ticketIdentifier: String, workflowRun: WorkflowRun): Mono<Ticket>
    fun assignTicket(ticketId: String, memberName: String): Mono<Unit>
    fun unassignTicket(ticketId: String): Mono<Unit>
}