package pm.gh.integration.repository.impl

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation.lookup
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.project
import org.springframework.data.mongodb.core.aggregation.Aggregation.unwind
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository
import pm.gh.integration.model.Project
import pm.gh.integration.model.Team
import pm.gh.integration.repository.ProjectRepository
import pm.gh.integration.util.toObjectId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProjectRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : ProjectRepository {
    override fun create(project: Project): Mono<Project> {
        return mongoTemplate.insert(project)
    }

    override fun findAllByTeamMemberId(teamMemberId: String): Flux<Project> {
        val aggregation = newAggregation(
            lookup().from(Team.COLLECTION_NAME)
                .localField(Project::teamId.name)
                .foreignField(Fields.UNDERSCORE_ID)
                .`as`("aggregatedTeam"),
            unwind("aggregatedTeam"),
            match(where(Team::teamMemberIds.name).isEqualTo(teamMemberId.toObjectId())),
            project().andExclude("aggregatedTeam")
        )
        return mongoTemplate.aggregate<Project>(aggregation, Project.COLLECTION_NAME)
    }

    override fun findByName(projectName: String): Mono<Project> {
        return mongoTemplate.findOne(query(where(Project::fullName.name).isEqualTo(projectName)))
    }

    override fun deleteById(id: String): Mono<Unit> {
        return mongoTemplate.remove(query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId())))
            .thenReturn(Unit)
    }

    override fun update(updatedProject: Project): Mono<Project> {
        return mongoTemplate.save(updatedProject)
    }

    override fun findById(id: String): Mono<Project> {
        return mongoTemplate.findById(id.toObjectId())
    }
}