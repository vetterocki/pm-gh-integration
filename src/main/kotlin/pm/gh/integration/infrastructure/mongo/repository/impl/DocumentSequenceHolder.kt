package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.findAndModify
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.infrastructure.mongo.model.DocumentSequence
import reactor.core.publisher.Mono

@Repository
class DocumentSequenceHolder(private val reactiveMongoOperations: ReactiveMongoOperations) {

    fun acquireSequenceCounter(sequenceName: String): Mono<Long> {
        val query = query(where(DocumentSequence::sequenceName.name).isEqualTo(sequenceName))
        val update = Update().inc(DocumentSequence::sequenceCounter.name, 1)
        val options = FindAndModifyOptions.options().returnNew(true).upsert(true)

        return reactiveMongoOperations
            .findAndModify<DocumentSequence>(query, update, options)
            .map { it.sequenceCounter }
            .defaultIfEmpty(1)
    }
}