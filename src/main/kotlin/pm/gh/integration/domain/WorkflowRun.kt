package pm.gh.integration.domain


data class WorkflowRun(
    val htmlUrl: String,
    val ticketIdentifier: String,
    val conclusion: String,
    val actor: Actor,
    val repositoryName: String
)