package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailRequestDTO;
import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailResponseDTO;
import com.elias.GestoBar.model.TicketDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketDetailMapper {
    TicketDetail toEntity(TicketDetailRequestDTO ticketDetailRequestDTO);
    TicketDetailResponseDTO toResponse(TicketDetail ticketDetail);
}
