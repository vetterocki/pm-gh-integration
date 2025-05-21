package pm.gh.integration.domain

data class PullRequestStatus(
    val branchRef: String,
    val status: Status,
) {
    enum class Status {
        STATUS_UNSPECIFIED,
        STATUS_MERGED,
        STATUS_CLOSED,
        STATUS_OPENED,
        STATUS_DRAFT,
    }
}
