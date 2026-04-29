package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.ticketDTO.TicketRequestDTO;
import com.elias.GestoBar.dto.ticketDTO.TicketResponseDTO;
import com.elias.GestoBar.model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TicketDetailMapper.class})
public interface TicketMapper {

    Ticket toEntity(TicketRequestDTO ticketRequestDTO);

    @Mapping(source = "table.tableId",  target = "tableId")
    @Mapping(source = "table.number",   target = "tableNumber")
    @Mapping(source = "user.userId",    target = "userId")
    @Mapping(source = "details",        target = "details")
    TicketResponseDTO toResponse(Ticket ticket);
}
