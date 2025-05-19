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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pm.gh.integration.rest.dto.TeamMemberDto
import pm.gh.integration.rest.dto.TeamMemberUpdateDto
import pm.gh.integration.rest.mapper.TeamMemberMapper.toDto
import pm.gh.integration.rest.mapper.TeamMemberMapper.toModel
import pm.gh.integration.service.TeamMemberService
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/members")
class TeamMemberController(private val teamMemberService: TeamMemberService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody teamDto: TeamMemberDto): Mono<TeamMemberDto> {
        return teamMemberService.create(teamDto.toModel()).let { it.map { created -> created.toDto() } }
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
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return teamMemberService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: String, teamMemberUpdateDto: TeamMemberUpdateDto): Mono<TeamMemberDto> {
        return teamMemberService.update(id, teamMemberUpdateDto).map { it.toDto() }
    }
}