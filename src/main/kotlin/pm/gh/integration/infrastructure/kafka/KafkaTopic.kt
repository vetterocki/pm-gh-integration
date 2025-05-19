package pm.gh.integration.infrastructure.kafka

object KafkaTopic {
    private const val GITHUB_APP_SERVICE_PREFIX = "pm_gh_integration.github_app"

    private const val OUTPUT_PREFIX = "$GITHUB_APP_SERVICE_PREFIX.output"
    private const val INPUT_PREFIX = "$GITHUB_APP_SERVICE_PREFIX.input"

    object PullRequest {
        private const val PULL_REQUEST_PREFIX = "$OUTPUT_PREFIX.pull_request"

        const val OPENED_TOPIC = "$PULL_REQUEST_PREFIX.opened"
        const val OPENED_CHANNEL = "pull_request-opened"
    }

    object TicketStatus {
        private const val TICKET_STATUS_PREFIX = "$INPUT_PREFIX.ticket_status"

        const val UPDATE_TOPIC = "$TICKET_STATUS_PREFIX.update"
        const val UPDATE_CHANNEL = "ticket_status-update"
    }

    object WorkflowRun {
        private const val WORKFLOW_RUN_PREFIX = "$OUTPUT_PREFIX.workflow_run"

        const val COMPLETED_TOPIC = "$WORKFLOW_RUN_PREFIX.completed"
        const val COMPLETED_CHANNEL = "workflow_run-completed"
    }
}
