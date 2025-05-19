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
import pm.gh.integration.rest.dto.TicketDto
import pm.gh.integration.rest.dto.TicketUpdateDto
import pm.gh.integration.rest.mapper.TicketMapper.toDto
import pm.gh.integration.rest.mapper.TicketMapper.toModel
import pm.gh.integration.service.TicketService
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody teamDto: TicketDto): Mono<TicketDto> {
        return ticketService.create(teamDto.toModel()).let { it.map { created -> created.toDto() } }
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
    fun update(@PathVariable id: String, ticketUpdateDto: TicketUpdateDto): Mono<TicketDto> {
        return ticketService.update(id, ticketUpdateDto).map { it.toDto() }
    }
}