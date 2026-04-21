package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.ticketDTO.TicketRequestDTO;
import com.elias.GestoBar.dto.ticketDTO.TicketResponseDTO;
import com.elias.GestoBar.model.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toEntity(TicketRequestDTO ticketRequestDTO);
    TicketResponseDTO toResponse(Ticket ticket);
}
