package pm.gh.integration.service.impl

import org.springframework.stereotype.Service
import pm.gh.integration.model.TeamMember
import pm.gh.integration.repository.TeamMemberRepository
import pm.gh.integration.rest.dto.TeamMemberUpdateDto
import pm.gh.integration.rest.mapper.ProjectMapper.partialUpdate
import pm.gh.integration.rest.mapper.TeamMemberMapper.partialUpdate
import pm.gh.integration.service.TeamMemberService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class TeamMemberServiceImpl(private val teamMemberRepository: TeamMemberRepository) : TeamMemberService {
    override fun create(teamMember: TeamMember): Mono<TeamMember> {
        return teamMemberRepository.create(teamMember)
    }

    override fun update(id: String, teamMemberUpdateDto: TeamMemberUpdateDto): Mono<TeamMember> {
        return getById(id)
            .map { it.partialUpdate(teamMemberUpdateDto) }
            .flatMap{ teamMemberRepository.update(it) }
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
}