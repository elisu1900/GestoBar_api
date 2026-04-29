package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailResponseDTO;
import com.elias.GestoBar.model.TicketDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketDetailMapper {

    @Mapping(source = "ticket.ticketId", target = "ticketId")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name",      target = "productName")
    TicketDetailResponseDTO toResponse(TicketDetail ticketDetail);
}