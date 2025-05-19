package pm.gh.integration.util

import org.bson.types.ObjectId

fun String.toObjectId(): ObjectId {
    return ObjectId(this)
}