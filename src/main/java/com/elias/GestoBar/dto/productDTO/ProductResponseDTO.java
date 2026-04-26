package com.elias.GestoBar.dto.productDTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Integer productId;
    private Integer categoryId;
    private String categoryName;
    private String name;
    private BigDecimal sellPrice;
    private BigDecimal costPrice;
    private Boolean isActive;
}