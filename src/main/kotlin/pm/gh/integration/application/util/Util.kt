package pm.gh.integration.application.util

import org.bson.types.ObjectId


fun String.toObjectId() = ObjectId(this)