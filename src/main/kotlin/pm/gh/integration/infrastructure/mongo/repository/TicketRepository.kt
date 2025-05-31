package pm.gh.integration.infrastructure.mongo.repository

import org.bson.types.ObjectId
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TicketRepository {
    fun create(ticket: Ticket): Mono<Ticket>
    fun save(ticket: Ticket): Mono<Ticket>
    fun findById(ticketId: String): Mono<Ticket>
    fun deleteById(id: String): Mono<Unit>
    fun update(ticket: Ticket): Mono<Ticket>
    fun findByTicketIdentifier(ticketIdentifier: String): Mono<Ticket>
    fun findAllByTicketIdentifierContaining(ticketIdentifier: String): Flux<Ticket>
    fun findAllByProjectBoardId(projectBoardId: String): Flux<Ticket>
    fun findAllByIdIn(ticketIds: List<String>): Flux<Ticket>
    fun findAllByProjectId(projectId: String): Flux<Ticket>
    fun findAllByProjectBoardIdGroupedByStatus(projectBoardId: String): Mono<Map<String, Flux<Ticket>>>
    fun updateTicketStatus(ticketIdentifier: String, status: TicketStatus): Mono<Ticket>
    fun updateTicketGithubDescription(ticketIdentifier: String, githubDescription: String): Mono<Ticket>
    fun updateTicketReviewers(ticketIdentifier: String, reviewers: List<ObjectId?>): Mono<Ticket>
    fun updateTicketPullRequests(ticketIdentifier: String, pullRequest: PullRequest): Mono<Ticket>
    fun updateTicketWorkflowRuns(ticketIdentifier: String, workflowRun: WorkflowRun): Mono<Ticket>
}