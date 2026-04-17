package com.elias.GestoBar.dto.ticketDetailDTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetailResponseDTO {

    private Integer ticketId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
