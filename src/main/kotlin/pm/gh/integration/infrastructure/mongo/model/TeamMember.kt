package pm.gh.integration.infrastructure.mongo.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pm.gh.integration.infrastructure.mongo.model.TeamMember.Companion.COLLECTION_NAME

@TypeAlias("TeamMember")
@Document(collection = COLLECTION_NAME)
data class TeamMember(
    @Id val id: ObjectId?,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val teamId: ObjectId?,
    val loginInGithub: String,
    val position: String,
    val avatarUrl: String?,
    private val password: String,
    val role: Role = Role.DEFAULT
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role.toString()))
    }

    override fun getPassword(): String? = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    companion object {
        const val COLLECTION_NAME = "team_members"
    }

    enum class Role {
        DEFAULT,
        MANAGER,
        ADMIN
    }
}
