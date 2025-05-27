package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Ticket.Companion.COLLECTION_NAME
import java.time.Instant


@TypeAlias("Ticket")
@Document(collection = COLLECTION_NAME)
data class Ticket(
    @Id
    val id: ObjectId?,
    val ticketIdentifier: String?,
    val projectId: ObjectId?,
    val projectBoardId: ObjectId?,
    val createdAt: Instant,
    val summary: String,
    val description: String?,
    val reporter: TeamMember?,
    val assignee: TeamMember?,
    val reviewerIds: List<ObjectId>?,
    val linkedTicketIds: List<ObjectId>?,
    val labels: List<ProjectLabel>?,
    val linkedPullRequests: List<PullRequest>?,
    val linkedWorkflowRuns: List<WorkflowRun>?,
    val priority: TicketPriority = TicketPriority.MINOR,
    val status: TicketStatus,
    val githubDescription: String?,
) {
    enum class TicketPriority {
        MINOR,
        MAJOR,
        CRITICAL,
        BLOCKER
    }

    companion object {
        const val COLLECTION_NAME = "tickets"
        const val SEQUENCE_NAME = "ticket_sequence"
    }
}
