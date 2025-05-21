package pm.gh.integration.infrastructure.kafka.mapper

import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.kafka.mapper.ActorMapper.toDomain
import pm.gh.integration.output.WorkflowRunCompletedEvent

object WorkflowRunMapper {
    fun WorkflowRunCompletedEvent.toDomain(): WorkflowRun {
        return WorkflowRun(
            htmlUrl = htmlUrl,
            ticketIdentifier = titleComposition.ticketIdentifier,
            conclusion = conclusion,
            actor = actor.toDomain(),
            repositoryName = repositoryName
        )
    }
}