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
import pm.gh.integration.application.service.ProjectLabelService
import pm.gh.integration.infrastructure.rest.dto.ProjectLabelDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectLabelMapper.toModel
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/labels")
class ProjectLabelController(private val projectLabelService: ProjectLabelService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody projectLabelDto: ProjectLabelDto): Mono<ProjectLabelDto> {
        return projectLabelService.create(projectLabelDto.toModel()).let { it.map { created -> created.toDto() } }
    }

    @GetMapping
    fun findByName(@RequestParam labelName: String): Mono<ResponseEntity<ProjectLabelDto>> {
        return projectLabelService.findByName(labelName)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<ProjectLabelDto>> {
        return projectLabelService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return projectLabelService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: String, projectLabelDto: ProjectLabelDto): Mono<ProjectLabelDto> {
        return projectLabelService.update(id, projectLabelDto).map { it.toDto() }
    }
}