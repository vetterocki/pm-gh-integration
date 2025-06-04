package pm.gh.integration.application.service

import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.cast

@Service
class UserDetailsServiceImpl(private val teamMemberService: TeamMemberService) : ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        return teamMemberService.findByEmail(username.orEmpty()).cast<UserDetails>()
    }
}