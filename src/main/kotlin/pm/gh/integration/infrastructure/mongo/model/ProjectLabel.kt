package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel.Companion.COLLECTION_NAME

@TypeAlias("ProjectLabel")
@Document(collection = COLLECTION_NAME)
data class ProjectLabel(
    val id: ObjectId?,
    val name: String,
) {
    companion object {
        const val COLLECTION_NAME = "project_labels"
    }
}