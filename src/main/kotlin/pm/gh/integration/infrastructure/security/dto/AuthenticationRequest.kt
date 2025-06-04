package pm.gh.integration.infrastructure.security.dto

import pm.gh.integration.infrastructure.rest.dto.TeamMemberDto

data class AuthenticationRequest(val email: String, val password: String)

data class AuthenticationResponse(val token: String, val teamMember: TeamMemberDto)