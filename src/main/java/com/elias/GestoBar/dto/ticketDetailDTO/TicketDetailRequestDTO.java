package com.elias.GestoBar.dto.ticketDetailDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetailRequestDTO {

    @NotNull
    private Integer productId;

    @NotNull
    @Min(1)
    private Integer quantity;

}
