package pm.gh.integration.infrastructure.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import pm.gh.integration.infrastructure.security.config.JwtProperties
import java.util.Date

@Component
@EnableConfigurationProperties(JwtProperties::class)
class JwtGenerator(private val jwtProperties: JwtProperties) {
    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperties.expirationInSeconds * 1000)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()), SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUsernameFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(jwtProperties.secret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.secret.toByteArray())
                .build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}

