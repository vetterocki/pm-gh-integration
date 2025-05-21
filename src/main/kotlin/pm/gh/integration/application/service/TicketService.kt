package pm.gh.integration.application.service

import org.bson.types.ObjectId
import pm.gh.integration.domain.Actor
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.rest.dto.TicketUpdateDto
import pm.gh.integration.input.UpdateTicketGithubDescription
import reactor.core.publisher.Mono

interface TicketService {
    fun create(ticket: Ticket, projectId: String, reporterId: String, assigneeId: String): Mono<Ticket>
    fun findById(ticketId: String): Mono<Ticket>
    fun deleteById(id: String): Mono<Unit>
    fun update(id: String, ticketUpdateDto: TicketUpdateDto): Mono<Ticket>
    fun getById(id: String): Mono<Ticket>
    fun findByTicketIdentifier(ticketIdentifier: String): Mono<Ticket>
    fun updateTicketStatus(ticketIdentifier: String, status: String): Mono<Ticket>
    fun updateTicketGithubDescription(ticketIdentifier: String, githubDescription: String): Mono<Ticket>
    fun updateTicketReviewers(ticketIdentifier: String, reviewers: List<Actor>): Mono<Ticket>
    fun updateTicketPullRequests(ticketIdentifier: String, pullRequest: PullRequest): Mono<Ticket>
    fun updateTicketWorkflowRuns(ticketIdentifier: String, workflowRun: WorkflowRun): Mono<Ticket>
}