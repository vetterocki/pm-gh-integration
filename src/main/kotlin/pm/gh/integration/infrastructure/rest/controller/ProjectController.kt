package pm.gh.integration.infrastructure.rest.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.infrastructure.rest.dto.ProjectDto
import pm.gh.integration.infrastructure.rest.dto.ProjectUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectMapper.toModel
import pm.gh.integration.infrastructure.security.annotations.HasAdminRole
import pm.gh.integration.infrastructure.security.annotations.HasManagerRole
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @HasManagerRole
    fun create(@Valid @RequestBody projectDto: ProjectDto): Mono<ProjectDto> {
        return projectService.create(
            projectDto.toModel(),
            teamName = projectDto.teamName,
            projectOwnerName = projectDto.projectOwnerName
        ).let { it.map { created -> created.toDto() } }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): Flux<ProjectDto> {
        return projectService.findAll().map { it.toDto() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<ProjectDto>> {
        return projectService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }


    @GetMapping
    fun findByName(@RequestParam projectName: String): Mono<ResponseEntity<ProjectDto>> {
        return projectService.findByName(projectName)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasAdminRole
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return projectService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @HasManagerRole
    fun update(@PathVariable id: String, @RequestBody projectUpdateDto: ProjectUpdateDto): Mono<ProjectDto> {
        return projectService.update(id, projectUpdateDto).map { it.toDto() }
    }
}