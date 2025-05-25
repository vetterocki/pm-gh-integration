package pm.gh.integration.application.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.domain.Actor
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.mongo.repository.TeamMemberRepository
import pm.gh.integration.infrastructure.rest.dto.TeamMemberUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.partialUpdate
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TeamMemberServiceImpl(
    private val teamMemberRepository: TeamMemberRepository,
) : TeamMemberService {
    override fun create(teamMember: TeamMember): Mono<TeamMember> {
        return teamMemberRepository.create(teamMember)
    }

    override fun update(id: String, teamMemberUpdateDto: TeamMemberUpdateDto): Mono<TeamMember> {
        return getById(id)
            .map { it.partialUpdate(teamMemberUpdateDto) }
            .flatMap { teamMemberRepository.update(it) }
    }

    override fun deleteById(teamMemberId: String): Mono<Unit> {
        return teamMemberRepository.deleteById(teamMemberId)
    }

    override fun findById(teamId: String): Mono<TeamMember> {
        return teamMemberRepository.findById(teamId)
    }

    override fun getById(id: String): Mono<TeamMember> {
        return findById(id).switchIfEmpty { Mono.error { RuntimeException("Team member not found by id $id") } }
    }

    override fun findByGithubCredentials(actor: Actor): Mono<TeamMember> {
        return teamMemberRepository.findByGithubCredentials(actor)
    }

    override fun findByNameOrEmail(credential: String): Mono<TeamMember> {
        return teamMemberRepository.findByNameOrEmail(credential)
    }
}