package com.elias.GestoBar.dto.balanceDTO;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyBalanceResponseDTO {

    private BigDecimal totalRevenue;
    private BigDecimal totalCosts;
    private BigDecimal realProfit;
    private List<ProductBalanceDTO> breakdown;
}