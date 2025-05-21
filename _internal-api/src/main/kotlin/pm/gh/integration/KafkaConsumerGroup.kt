package pm.gh.integration

object KafkaConsumerGroup {
    private const val DOMAIN_SERVICE_PREFIX = "pm_gh_integration"

    const val UPDATE_TICKET_STATUS_GROUP = "$DOMAIN_SERVICE_PREFIX.update.ticket.status.group"
    const val UPDATE_TICKET_REVIEWERS_GROUP = "$DOMAIN_SERVICE_PREFIX.update.ticket.reviewers.group"
    const val UPDATE_TICKET_GITHUB_DESCRIPTION_GROUP = "$DOMAIN_SERVICE_PREFIX.update.ticket.github.description.group"
    const val PULL_REQUEST_STATUS_GROUP = "$DOMAIN_SERVICE_PREFIX.pull_request.status.group"
    const val WORKFLOW_RUN_COMPLETED_GROUP = "$DOMAIN_SERVICE_PREFIX.workflow_run.completed.group"
}