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
import pm.gh.integration.infrastructure.rest.dto.TeamMemberDto
import pm.gh.integration.infrastructure.rest.dto.TeamMemberUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.toModel
import pm.gh.integration.infrastructure.security.annotations.HasManagerRole
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/members")
class TeamMemberController(private val teamMemberService: TeamMemberService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody teamDto: TeamMemberDto): Mono<TeamMemberDto> {
        return teamMemberService.create(teamDto.toModel(), teamDto.teamId).map { it.toDto() }
    }

    @GetMapping("/all")
    fun findAll(): Flux<TeamMemberDto> {
        return teamMemberService.findAll().map { it.toDto() }
    }

    @GetMapping
    fun findByName(@RequestParam teamMemberName: String): Mono<ResponseEntity<TeamMemberDto>> {
        return teamMemberService.findByNameOrEmail(teamMemberName)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<TeamMemberDto>> {
        return teamMemberService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @HasManagerRole
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return teamMemberService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable id: String,
        @RequestBody teamMemberUpdateDto: TeamMemberUpdateDto,
        principal: Principal,
    ): Mono<TeamMemberDto> {
        return teamMemberService.update(id, teamMemberUpdateDto, principal.name).map { it.toDto() }
    }
}