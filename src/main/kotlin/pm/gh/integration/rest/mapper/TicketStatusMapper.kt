package pm.gh.integration.rest.mapper

import pm.gh.integration.model.TicketStatus
import pm.gh.integration.rest.dto.TicketStatusDto

object TicketStatusMapper {
    fun TicketStatus.toDto(): TicketStatusDto = TicketStatusDto(name)

    fun TicketStatusDto.toModel(): TicketStatus = TicketStatus(id = null, name = name)

    fun TicketStatus.partialUpdate(dto: TicketStatusDto): TicketStatus = copy(name = dto.name)
}