package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.infrastructure.mongo.model.TeamMember.Companion.COLLECTION_NAME

@TypeAlias("TeamMember")
@Document(collection = COLLECTION_NAME)
data class TeamMember(
    @Id val id: ObjectId?,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val teamId: ObjectId?,
    val loginInGithub: String,
    val position: String,
    val avatarUrl: String?,
) {
    companion object {
        const val COLLECTION_NAME = "team_members"
    }
}