package pm.gh.integration.infrastructure.rest.mapper

import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import pm.gh.integration.infrastructure.rest.dto.TicketStatusDto

object TicketStatusMapper {
    fun TicketStatus.toDto(): TicketStatusDto = TicketStatusDto(
        id = id.toString(),
        name = name
    )

    fun TicketStatusDto.toModel(): TicketStatus = TicketStatus(id = null, name = name)

    fun TicketStatus.partialUpdate(dto: TicketStatusDto): TicketStatus = copy(name = dto.name)
}