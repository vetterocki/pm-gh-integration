package pm.gh.integration.infrastructure.mongo.repository.impl

import org.bson.types.ObjectId
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAndModify
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.mongo.repository.TicketRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class TicketRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : TicketRepository {

    override fun create(ticket: Ticket): Mono<Ticket> {
        return mongoTemplate.insert(ticket)
    }

    override fun save(ticket: Ticket): Mono<Ticket> {
        return mongoTemplate.save(ticket)
    }

    override fun findById(ticketId: String): Mono<Ticket> {
        return mongoTemplate.findById(ticketId.toObjectId())
    }

    override fun deleteById(id: String): Mono<Unit> {
        return mongoTemplate.remove<Ticket>(query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId()))).thenReturn(Unit)
    }

    override fun update(ticket: Ticket): Mono<Ticket> {
        return mongoTemplate.save(ticket)
    }

    override fun findByTicketIdentifier(ticketIdentifier: String): Mono<Ticket> {
        return mongoTemplate.findOne<Ticket>(query(where(Ticket::ticketIdentifier.name).isEqualTo(ticketIdentifier)))
    }

    override fun findAllByTicketIdentifierContaining(ticketIdentifier: String): Flux<Ticket> {
        val regex = ".*$ticketIdentifier.*"
        return mongoTemplate.find<Ticket>(
            query(where(Ticket::ticketIdentifier.name).regex(regex, "i")),
        )
    }

    override fun findAllByProjectBoardId(projectBoardId: String): Flux<Ticket> {
        return mongoTemplate.find<Ticket>(
            query(
                where(Ticket::projectBoardId.name).isEqualTo(projectBoardId.toObjectId()),
            ).with(Sort.by(Sort.Direction.ASC, Ticket::createdAt.name))
        )
    }

    override fun findAllByIdIn(ticketIds: List<String>): Flux<Ticket> {
        return mongoTemplate.find<Ticket>(
            query(
                where(Fields.UNDERSCORE_ID).`in`(ticketIds.map { it.toObjectId() })
            ).with(Sort.by(Sort.Direction.ASC, Ticket::ticketIdentifier.name)))
    }

    override fun findAllByProjectId(projectId: String): Flux<Ticket> {
        return mongoTemplate.find<Ticket>(
            query(
                where(Ticket::projectId.name).isEqualTo(projectId.toObjectId()),
            ).with(Sort.by(Sort.Direction.ASC, Ticket::createdAt.name))
        )
    }

    override fun findAllByProjectBoardIdGroupedByStatus(projectBoardId: String): Mono<Map<String, Flux<Ticket>>> {
        return mongoTemplate.find<Ticket>(
            query(where(Ticket::projectBoardId.name).isEqualTo(projectBoardId.toObjectId()))
        ).groupBy { it.status.name }.collectMap({ it.key() }, { it })
    }

    override fun updateTicketStatus(ticketIdentifier: String, status: TicketStatus): Mono<Ticket> {
        return updateTicketField(ticketIdentifier, Update().set(Ticket::status.name, status))
    }

    override fun updateTicketGithubDescription(ticketIdentifier: String, githubDescription: String): Mono<Ticket> {
        return updateTicketField(ticketIdentifier, Update().set(Ticket::githubDescription.name, githubDescription))
    }

    override fun updateTicketReviewers(ticketIdentifier: String, reviewers: List<ObjectId?>): Mono<Ticket> {
        return updateTicketField(ticketIdentifier, Update().set(Ticket::reviewerIds.name, reviewers))
    }

    override fun updateTicketPullRequests(ticketIdentifier: String, pullRequest: PullRequest): Mono<Ticket> {
        return updateTicketField(ticketIdentifier, Update().push(Ticket::linkedPullRequests.name, pullRequest))
    }

    override fun updateTicketWorkflowRuns(
        ticketIdentifier: String, workflowRun: WorkflowRun,
    ): Mono<Ticket> {
        return updateTicketField(ticketIdentifier, Update().push(Ticket::linkedWorkflowRuns.name, workflowRun))
    }

    private fun updateTicketField(ticketIdentifier: String, update: Update): Mono<Ticket> {
        return mongoTemplate.findAndModify<Ticket>(
            query(where(Ticket::ticketIdentifier.name).isEqualTo(ticketIdentifier)),
            update,
            FindAndModifyOptions.options().returnNew(true)
        )
    }

}