package pm.gh.integration.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.model.Ticket.Companion.COLLECTION_NAME


@TypeAlias("Ticket")
@Document(collection = COLLECTION_NAME)
data class Ticket(
    @Id val id: ObjectId?,
    val projectId: ObjectId,
    val summary: String,
    val description: String?,
    val reporterId: ObjectId,
    val assigneeId: ObjectId,
    // TODO remodel
    val linkedTicketIds: List<ObjectId>?,
    val priority: TicketPriority = TicketPriority.MINOR,
    val status: TicketStatus,
) {
    enum class TicketPriority {
        MINOR,
        MAJOR,
        CRITICAL,
        BLOCKER
    }

    companion object {
        const val COLLECTION_NAME = "tickets"
    }
}