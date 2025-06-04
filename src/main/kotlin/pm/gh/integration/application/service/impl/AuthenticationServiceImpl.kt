package pm.gh.integration.application.service.impl

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pm.gh.integration.application.service.AuthenticationService
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.mongo.repository.TeamMemberRepository
import pm.gh.integration.infrastructure.security.jwt.JwtGenerator
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class AuthenticationServiceImpl(
    private val teamMemberRepository: TeamMemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtGenerator: JwtGenerator,
) : AuthenticationService {

    override fun login(
        email: String,
        password: String,
    ): Mono<Pair<String, TeamMember>> {
        return teamMemberRepository.findByEmail(email)
            .filter {
                passwordEncoder.matches(password, it.password)
            }
            .map { teamMember -> jwtGenerator.generateToken(email) to teamMember }
            .switchIfEmpty { Mono.error(AccessDeniedException("Invalid email or password")) }
    }

}