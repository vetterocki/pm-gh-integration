package pm.gh.integration.application.service

import pm.gh.integration.infrastructure.mongo.model.TeamMember
import reactor.core.publisher.Mono

interface AuthenticationService {
    fun login(email: String, password: String): Mono<Pair<String, TeamMember>>
}