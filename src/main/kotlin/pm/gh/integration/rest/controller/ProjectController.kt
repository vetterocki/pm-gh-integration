package pm.gh.integration.rest.controller

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
import pm.gh.integration.rest.dto.ProjectDto
import pm.gh.integration.rest.dto.ProjectUpdateDto
import pm.gh.integration.rest.mapper.ProjectMapper.toDto
import pm.gh.integration.rest.mapper.ProjectMapper.toModel
import pm.gh.integration.service.ProjectService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody projectDto: ProjectDto): Mono<ProjectDto> {
        return projectService.create(projectDto.toModel()).let { it.map { created -> created.toDto() } }
    }


    @GetMapping("/{member-id}")
    @ResponseStatus(HttpStatus.OK)
    fun findAllByTeamMemberId(@PathVariable("member-id") memberId: String): Flux<ProjectDto> {
        return projectService.findAllByTeamMemberId(memberId).map { it.toDto() }
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
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return projectService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: String, projectUpdateDto: ProjectUpdateDto): Mono<ProjectDto> {
        return projectService.update(id, projectUpdateDto).map { it.toDto() }
    }
}