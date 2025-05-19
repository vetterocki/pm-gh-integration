package pm.gh.integration.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.model.TicketStatus.Companion.COLLECTION_NAME

@TypeAlias("TicketStatus")
@Document(collection = COLLECTION_NAME)
data class TicketStatus(
    val id: ObjectId?,
    val name: String,
) {
    companion object {
        const val COLLECTION_NAME = "ticket_statuses"
    }
}