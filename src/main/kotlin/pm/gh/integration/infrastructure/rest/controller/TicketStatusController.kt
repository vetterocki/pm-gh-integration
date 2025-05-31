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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pm.gh.integration.application.service.TicketStatusService
import pm.gh.integration.infrastructure.rest.dto.TicketStatusDto
import pm.gh.integration.infrastructure.rest.mapper.TicketStatusMapper.toDto
import pm.gh.integration.infrastructure.rest.mapper.TicketStatusMapper.toModel
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/statuses")
class TicketStatusController(private val ticketStatusService: TicketStatusService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody teamDto: TicketStatusDto): Mono<TicketStatusDto> {
        return ticketStatusService.create(teamDto.toModel()).let { it.map { created -> created.toDto() } }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ResponseEntity<TicketStatusDto>> {
        return ticketStatusService.findById(id)
            .map { it.toDto() }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String): Mono<Unit> {
        return ticketStatusService.deleteById(id)
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: String, @RequestBody ticketStatusDto: TicketStatusDto): Mono<TicketStatusDto> {
        return ticketStatusService.update(id, ticketStatusDto).map { it.toDto() }
    }
}