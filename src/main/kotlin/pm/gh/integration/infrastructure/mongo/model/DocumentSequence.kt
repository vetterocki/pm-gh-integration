package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import pm.gh.integration.infrastructure.mongo.model.DocumentSequence.Companion.COLLECTION_NAME

@TypeAlias("DocumentSequence")
@Document(collection = COLLECTION_NAME)
data class DocumentSequence(
    @Id val id: ObjectId,
    val sequenceName: String,
    val sequenceCounter: Long = 0,
) {
    companion object {
        const val COLLECTION_NAME = "sequences"
    }
}
