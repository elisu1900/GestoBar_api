package com.elias.GestoBar.dto.ticketDTO;

import com.elias.GestoBar.dto.ticketDetailDTO.TicketDetailRequestDTO;
import com.elias.GestoBar.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequestDTO {

    @NotNull
    private Integer tableId;

    @NotNull
    private Integer userId;

    @NotNull
    private TicketStatus status;

    @Builder.Default
    private List<TicketDetailRequestDTO> details = new ArrayList<>();
}
