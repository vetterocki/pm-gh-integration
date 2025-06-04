package pm.gh.integration.infrastructure.rest.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pm.gh.integration.application.service.AuthenticationService
import pm.gh.integration.infrastructure.security.dto.AuthenticationRequest
import pm.gh.integration.infrastructure.security.dto.AuthenticationResponse
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.toDto
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/login")
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): Mono<ResponseEntity<AuthenticationResponse>> {
        return authenticationService.login(authenticationRequest.email, authenticationRequest.password)
            .map { (token, teamMember) -> AuthenticationResponse(token, teamMember.toDto()) }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
    }
}