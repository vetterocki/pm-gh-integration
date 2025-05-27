package pm.gh.integration.infrastructure.rest.dto

import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.WorkflowRun
import java.time.Instant

data class TicketDto(
    val id: String?,
    val projectId: String,
    val projectBoardId: String,
    val summary: String,
    val description: String?,
    val reporter: TeamMemberDto?,
    val assignee: TeamMemberDto?,
    val linkedTicketIds: List<String>?,
    val priority: String,
    val status: String,
    val labels: List<ProjectLabelDto>?,
    val ticketIdentifier: String,
    val linkedPullRequests: List<PullRequest>?,
    val linkedWorkflowRuns: List<WorkflowRun>?,
    val githubDescription: String?,
    val createdAt: Instant?,
)