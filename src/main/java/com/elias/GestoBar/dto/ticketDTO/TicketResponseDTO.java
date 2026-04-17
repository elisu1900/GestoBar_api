package com.elias.GestoBar.dto.ticketDTO;

import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponseDTO {

    private Integer ticketId;
    private Integer tableId;
    private Integer tableNumber;
    private Integer userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    @Builder.Default
    private List<TicketDetailResponseDTO> details = new ArrayList<>();
}
