package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.infrastructure.mongo.model.ProjectBoard.Companion.COLLECTION_NAME

@TypeAlias("ProjectBoard")
@Document(collection = COLLECTION_NAME)
data class ProjectBoard(
    @Id val id: ObjectId?,
    val name: String,
    val projectId: ObjectId?,
    val ticketIds: List<ObjectId>?,
) {
    companion object {
        const val COLLECTION_NAME = "project_boards"
    }
}