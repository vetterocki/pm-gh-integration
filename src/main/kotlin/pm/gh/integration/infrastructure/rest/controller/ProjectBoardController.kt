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
import pm.gh.integration.application.service.ProjectBoardService
import pm.gh.integration.infrastructure.rest.dto.ProjectBoardDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectBoardMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.ProjectBoardMapper.toModel
import pm.gh.integration.infrastructure.security.annotations.HasManagerRole
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/boards")
class ProjectBoardController(private val projectBoardService: ProjectBoardService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @HasManagerRole
    fun create(@Valid @RequestBody projectDto: ProjectBoardDto): Mono<ProjectBoardDto> {
        return projectBoardService.create(projectDto.toModel()).let { it.map { created -> created.toDto() } }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<ProjectBoardDto>> {
        return projectBoardService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasManagerRole
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return projectBoardService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @HasManagerRole
    fun update(@PathVariable id: String, @RequestBody projectBoardDto: ProjectBoardDto): Mono<ProjectBoardDto> {
        return projectBoardService.update(id, projectBoardDto).map { it.toDto() }
    }

    @GetMapping
    fun findAllByProjectId(@RequestParam projectId: String): Flux<ProjectBoardDto> {
        return projectBoardService.findAllByProjectId(projectId).map { it.toDto() }
    }
}