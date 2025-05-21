package pm.gh.integration.domain


data class PullRequest(
    val htmlUrl: String,
    val ticketIdentifier: String,
    val title: String,
    val actor: Actor,
    val pullRequestStatus: PullRequestStatus,
    val repositoryName: String
)