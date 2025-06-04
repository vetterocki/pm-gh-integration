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
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.infrastructure.rest.dto.TeamMemberDto
import pm.gh.integration.infrastructure.rest.dto.TicketCreateDto
import pm.gh.integration.infrastructure.rest.dto.TicketDto
import pm.gh.integration.infrastructure.rest.dto.TicketUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.TicketMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.TicketMapper.toModel
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService, private val teamMemberService: TeamMemberService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody ticketCreateDto: TicketCreateDto): Mono<TicketDto> {
        return ticketService.create(
            ticketCreateDto.toModel(),
            ticketCreateDto.projectId,
            projectBoardId = ticketCreateDto.projectId,
            reporterId = ticketCreateDto.reporterId,
            labelsIds = ticketCreateDto.labels.orEmpty()
        ).let { it.map { created -> created.toDto() } }
    }

    @GetMapping("/{id}/linked")
    fun findAllLinkedTickets(@PathVariable id: String): Flux<TicketDto> {
        return ticketService.findAllByIdIn(id).map { it.toDto() }
    }

    @GetMapping("/{id}/reviewers")
    fun findAllReviewers(@PathVariable id: String): Flux<TeamMemberDto> {
        return ticketService.findById(id)
            .map { it.reviewerIds?.map { it.toString() }.orEmpty() }
            .flatMapMany { teamMemberService.findAllByIdIn(it) }
            .map { it.toDto() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<TicketDto>> {
        return ticketService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return ticketService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: String, @RequestBody ticketUpdateDto: TicketUpdateDto): Mono<TicketDto> {
        return ticketService.update(id, ticketUpdateDto).map { it.toDto() }
    }

    @GetMapping("/search")
    fun findAllByTicketIdentifierContaining(@RequestParam query: String): Flux<TicketDto> {
        return ticketService.findAllByTicketIdentifierContaining(query)
            .map { it.toDto() }
    }

    @GetMapping("/project/{projectId}")
    fun findAllByProjectId(@PathVariable projectId: String): Flux<TicketDto> {
        return ticketService.findAllByProjectId(projectId).map { it.toDto() }
    }

    @GetMapping("/project-board/{projectBoardId}")
    fun findAllByProjectBoardId(@PathVariable projectBoardId: String): Flux<TicketDto> {
        return ticketService.findAllByProjectBoardId(projectBoardId).map { it.toDto() }
    }

    @GetMapping("/project-board/{projectBoardId}/grouped-by-status")
    fun findAllByProjectBoardIdGroupedByStatus(@PathVariable projectBoardId: String): Mono<Map<String, List<TicketDto>>> {
        return ticketService.findAllByProjectBoardIdGroupedByStatus(projectBoardId)
            .flatMap { groupedMap ->
                Flux.fromIterable(groupedMap.entries)
                    .flatMap { (status, fluxTickets) ->
                        fluxTickets
                            .map { it.toDto() }
                            .collectList()
                            .map { list -> status to list }
                    }
                    .collectMap({ it.first }, { it.second })
            }

    }

    @PostMapping("/{id}/assign")
    @ResponseStatus(HttpStatus.OK)
    fun assignTicket(@PathVariable id: String, @RequestParam memberName: String): Mono<TicketDto> {
        return ticketService.assignTicket(id, memberName).map { it.toDto() }
    }

    @PostMapping("/{id}/unassign")
    @ResponseStatus(HttpStatus.OK)
    fun unassignTicket(@PathVariable id: String): Mono<Unit> {
        return ticketService.unassignTicket(id)
    }
}