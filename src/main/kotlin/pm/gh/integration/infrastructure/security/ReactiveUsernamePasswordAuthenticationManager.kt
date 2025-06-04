package pm.gh.integration.infrastructure.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component
import pm.gh.integration.infrastructure.security.jwt.JwtGenerator
import reactor.core.publisher.Mono

@Component
class ReactiveUsernamePasswordAuthenticationManager(
    private val jwtGenerator: JwtGenerator,
    private val userDetailsService: ReactiveUserDetailsService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val token = authentication?.credentials.toString()
        if (!jwtGenerator.validateToken(token)) return Mono.empty()

        val username = jwtGenerator.getUsernameFromToken(token)
        return userDetailsService.findByUsername(username)
            .map { user -> UsernamePasswordAuthenticationToken(user.username, null, user.authorities) }
    }
}