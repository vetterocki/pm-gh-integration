package pm.gh.integration.infrastructure.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.stereotype.Component
import pm.gh.integration.infrastructure.security.ReactiveUsernamePasswordAuthenticationManager
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationWebFilter(
    reactiveUsernamePasswordAuthenticationManager: ReactiveUsernamePasswordAuthenticationManager,
) : AuthenticationWebFilter(reactiveUsernamePasswordAuthenticationManager) {

    init {
        setServerAuthenticationConverter { exchange ->
            val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            if (authHeader?.startsWith("Bearer ") == true) {
                val token = authHeader.removePrefix("Bearer ").trim()
                Mono.just(UsernamePasswordAuthenticationToken(token, token))
            } else {
                Mono.empty()
            }
        }
    }
}

