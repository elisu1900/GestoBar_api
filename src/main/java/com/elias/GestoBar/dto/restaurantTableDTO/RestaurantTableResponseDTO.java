package com.elias.GestoBar.dto.restaurantTableDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTableResponseDTO {

    private Integer tableId;
    private Integer number;
    private Integer capacity;
    private Boolean isActive;
}
