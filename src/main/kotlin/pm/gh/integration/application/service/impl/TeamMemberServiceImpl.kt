package pm.gh.integration.application.service.impl

import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.application.service.TeamService
import pm.gh.integration.domain.Actor
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.mongo.repository.TeamMemberRepository
import pm.gh.integration.infrastructure.rest.dto.TeamMemberUpdateDto
import pm.gh.integration.infrastructure.rest.mapper.TeamMemberMapper.partialUpdate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Service
class TeamMemberServiceImpl(
    private val teamMemberRepository: TeamMemberRepository,
    private val teamService: TeamService,
) : TeamMemberService {
    override fun create(teamMember: TeamMember, teamId: String): Mono<TeamMember> {
        return teamService.findById(teamId)
            .map { teamMember.copy(teamId = it.id) }
            .flatMap { teamMemberRepository.create(it)}
            .flatMap { teamService. addMember(teamId, it).thenReturn(it) }
    }

    override fun update(id: String, teamMemberUpdateDto: TeamMemberUpdateDto, principal: String): Mono<TeamMember> {
        return teamMemberUpdateDto.email.toMono()
            .filter { it == principal }
            .switchIfEmpty { Mono.error { AccessDeniedException("You are not authorized to access this user.") } }
            .flatMap { getById(id) }
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

    override fun findByEmail(email: String): Mono<TeamMember> {
        return teamMemberRepository.findByEmail(email)
    }

    override fun findAllByIdIn(ticketIds: List<String>): Flux<TeamMember> {
        return teamMemberRepository.findAllByIdIn(ticketIds)
    }

    override fun findAll(): Flux<TeamMember> {
        return teamMemberRepository.findAll()
    }

    override fun save(teamMember: TeamMember): Mono<TeamMember> {
        return teamMemberRepository.save(teamMember)
    }
}