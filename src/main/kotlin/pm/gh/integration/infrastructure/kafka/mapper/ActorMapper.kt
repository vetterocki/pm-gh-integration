package pm.gh.integration.infrastructure.kafka.mapper

import pm.gh.integration.domain.Actor
import pm.gh.integration.common.Actor as ProtoActor

object ActorMapper {
    fun ProtoActor.toDomain(): Actor {
        return Actor(
            email = email.orEmpty(),
            login = login.orEmpty(),
            name = name.orEmpty()
        )
    }
}