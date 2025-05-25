package pm.gh.integration.infrastructure.rest.dto

import jakarta.validation.constraints.NotBlank

data class ProjectDto(
    val id: String?,
    @NotBlank
    val fullName: String,
    val key: String,
    val projectBoardIds: List<String>?,
    @NotBlank
    val teamName: String,
    @NotBlank
    val projectOwnerName: String,
    val projectLabelIds: List<String>?,
)