package pm.gh.integration.rest.dto

import jakarta.validation.constraints.NotBlank

data class ProjectDto(
    @NotBlank
    val fullName: String,
    val key: String,
    val projectBoardIds: List<String>?,
    @NotBlank
    val teamId: String,
    @NotBlank
    val projectOwnerId: String,
    val projectLabelIds: List<String>?,
)