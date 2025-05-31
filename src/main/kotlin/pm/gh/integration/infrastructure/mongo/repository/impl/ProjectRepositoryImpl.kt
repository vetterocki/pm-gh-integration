package pm.gh.integration.infrastructure.mongo.repository.impl

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation.lookup
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.project
import org.springframework.data.mongodb.core.aggregation.Aggregation.unwind
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findAndModify
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Repository
import pm.gh.integration.application.util.toObjectId
import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.mongo.repository.ProjectRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProjectRepositoryImpl(private val mongoTemplate: ReactiveMongoTemplate) : ProjectRepository {
    override fun findAll(): Flux<Project> {
        val query = Query().with(Sort.by(Sort.Direction.ASC, Project::fullName.name))
        return mongoTemplate.find<Project>(query)
    }


    override fun create(project: Project): Mono<Project> {
        return mongoTemplate.insert(project)
    }

    override fun findAllByTeamMemberId(teamMemberId: String): Flux<Project> {
        val aggregation = newAggregation(
            lookup().from(Team.COLLECTION_NAME)
                .localField(Project::team.name)
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
        return mongoTemplate.remove<Project>(query(where(Fields.UNDERSCORE_ID).isEqualTo(id.toObjectId())))
            .thenReturn(Unit)
    }

    override fun update(updatedProject: Project): Mono<Project> {
        return mongoTemplate.save(updatedProject)
    }

    override fun findById(id: String): Mono<Project> {
        return mongoTemplate.findById(id.toObjectId())
    }

    override fun addLabelToProject(
        projectLabel: ProjectLabel,
        projectId: String,
    ): Mono<Unit> {
        return mongoTemplate.findAndModify<Project>(
            query(where(Fields.UNDERSCORE_ID).isEqualTo(projectId.toObjectId())),
            Update().push(Project::projectLabelIds.name, projectLabel.id),
            FindAndModifyOptions.options().returnNew(true)
        ).thenReturn(Unit)
    }

    override fun removeLabelFromProject(
        projectLabel: ProjectLabel,
        projectId: String,
    ): Mono<Unit> {
        return mongoTemplate.findAndModify<Project>(
            query(where(Fields.UNDERSCORE_ID).isEqualTo(projectId.toObjectId())),
            Update().pull(Project::projectLabelIds.name, projectLabel.id),
            FindAndModifyOptions.options().returnNew(true)
        ).thenReturn(Unit)
    }
}