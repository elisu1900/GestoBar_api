package com.elias.GestoBar.dto.balanceDTO;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBalanceDTO {

    private String productName;
    private Integer qtySold;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal profit;
}