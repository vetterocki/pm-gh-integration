package pm.gh.integration

object KafkaTopic {
    private const val GITHUB_APP_SERVICE_PREFIX = "pm_gh_integration.github_app"

    private const val OUTPUT_PREFIX = "$GITHUB_APP_SERVICE_PREFIX.output"
    private const val INPUT_PREFIX = "$GITHUB_APP_SERVICE_PREFIX.input"

    object PullRequest {
        private const val PULL_REQUEST_PREFIX = "$OUTPUT_PREFIX.pull_request"

        const val STATUS_TOPIC = "$PULL_REQUEST_PREFIX.status"
        const val STATUS_CHANNEL = "pull_request-status"
    }

    object TicketStatus {
        private const val TICKET_STATUS_PREFIX = "$INPUT_PREFIX.ticket_status"

        const val UPDATE_TOPIC = "$TICKET_STATUS_PREFIX.update"
        const val UPDATE_CHANNEL = "ticket_status-update"
    }

    object TicketReviewers {
        private const val TICKET_REVIEWERS_PREFIX = "$INPUT_PREFIX.ticket_reviewers"

        const val UPDATE_TOPIC = "$TICKET_REVIEWERS_PREFIX.update"
        const val UPDATE_CHANNEL = "ticket_reviewers-update"
    }

    object TicketBranches {
        private const val TICKET_BRANCHES_PREFIX = "$INPUT_PREFIX.ticket_branches"

        const val UPDATE_TOPIC = "$TICKET_BRANCHES_PREFIX.update"
        const val UPDATE_CHANNEL = "ticket_branches-update"
    }

    object TicketGithubDescription {
        private const val TICKET_GITHUB_DESCRIPTION_PREFIX = "$INPUT_PREFIX.ticket_github_description"

        const val UPDATE_TOPIC = "$TICKET_GITHUB_DESCRIPTION_PREFIX.update"
        const val UPDATE_CHANNEL = "ticket_github_description-update"
    }


    object WorkflowRun {
        private const val WORKFLOW_RUN_PREFIX = "$OUTPUT_PREFIX.workflow_run"

        const val COMPLETED_TOPIC = "$WORKFLOW_RUN_PREFIX.completed"
        const val COMPLETED_CHANNEL = "workflow_run-completed"
    }

}
