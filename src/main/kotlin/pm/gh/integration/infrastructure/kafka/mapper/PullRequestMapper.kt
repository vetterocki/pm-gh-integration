package pm.gh.integration.infrastructure.kafka.mapper

import pm.gh.integration.domain.PullRequest
import pm.gh.integration.infrastructure.kafka.mapper.ActorMapper.toDomain
import pm.gh.integration.infrastructure.kafka.mapper.PullRequestStatusMapper.toDomain
import pm.gh.integration.output.PullRequestStatusEvent

object PullRequestMapper {
    fun PullRequestStatusEvent.toDomain(): PullRequest {
        return PullRequest(
            htmlUrl = htmlUrl,
            ticketIdentifier = titleComposition.ticketIdentifier,
            title = titleComposition.ticketSummary,
            actor = actor.toDomain(),
            pullRequestStatus = pullRequestStatus.toDomain(),
            repositoryName  = repositoryName,
        )
    }
}