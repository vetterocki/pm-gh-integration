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
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.application.service.TeamService
import pm.gh.integration.infrastructure.rest.dto.TeamDto
import pm.gh.integration.infrastructure.rest.dto.TeamUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMapper.toModel
import pm.gh.integration.infrastructure.security.annotations.HasAdminRole
import pm.gh.integration.infrastructure.security.annotations.HasManagerRole
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/teams")
class TeamController(private val teamService: TeamService, private val teamMemberService: TeamMemberService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @HasAdminRole
    fun create(@Valid @RequestBody teamDto: TeamDto): Mono<TeamDto> {
        return teamService.create(
            teamDto.toModel(),
            projectManagerName = teamDto.projectManagerName
        ).let { it.map { created -> created.toDto() } }
    }

    @GetMapping
    fun findByName(@RequestParam teamName: String): Mono<ResponseEntity<TeamDto>> {
        return teamService.findByName(teamName)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @GetMapping("/all")
    fun findAll(): Flux<TeamDto> {
        return teamService.findAll().map { it.toDto() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<TeamDto>> {
        return teamService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasAdminRole
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return teamService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @HasManagerRole
    fun update(@PathVariable id: String, @RequestBody teamUpdateDto: TeamUpdateDto): Mono<TeamDto> {
        return teamService.update(id, teamUpdateDto).map { it.toDto() }
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasManagerRole
    fun deleteMember(@PathVariable id: String, @PathVariable memberId: String): Mono<Unit> {
        return teamMemberService.findById(memberId)
            .flatMap { teamService.removeMember(id, it) }
            .thenReturn(Unit)
    }
}