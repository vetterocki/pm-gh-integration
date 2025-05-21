package pm.gh.integration.infrastructure.kafka.mapper

import pm.gh.integration.domain.PullRequestStatus
import pm.gh.integration.common.PullRequestStatus as PullRequestStatusProto

object PullRequestStatusMapper {
    fun PullRequestStatusProto.toDomain(): PullRequestStatus {
        return PullRequestStatus(
            branchRef = branchRef,
            status = PullRequestStatus.Status.valueOf(status.name)
        )
    }
}