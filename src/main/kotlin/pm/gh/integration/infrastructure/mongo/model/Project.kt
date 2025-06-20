package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.infrastructure.mongo.model.Project.Companion.COLLECTION_NAME


@TypeAlias("Project")
@Document(collection = COLLECTION_NAME)
data class Project(
    @Id val id: ObjectId?,
    val fullName: String,
    val key: String,
    val projectBoardIds: List<ObjectId>? = listOf(),
    val team: Team?,
    val projectOwner: TeamMember?,
    val projectLabelIds: List<ObjectId>? = listOf(),
) {
    companion object {
        const val COLLECTION_NAME = "projects"
    }
}