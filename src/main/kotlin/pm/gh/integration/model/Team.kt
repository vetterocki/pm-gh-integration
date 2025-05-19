package pm.gh.integration.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.model.Team.Companion.COLLECTION_NAME

@TypeAlias("Team")
@Document(collection = COLLECTION_NAME)
data class Team(
    @Id val id: ObjectId?,
    val name: String,
    val projectManagerId: ObjectId?,
    val teamMemberIds: List<ObjectId>?,
    val projectIds: List<ObjectId>?,
) {
    companion object {
        const val COLLECTION_NAME = "teams"
    }
}