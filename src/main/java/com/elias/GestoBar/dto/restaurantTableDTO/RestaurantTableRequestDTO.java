package com.elias.GestoBar.dto.restaurantTableDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTableRequestDTO {

    @NotNull
    @Min(1)
    private Integer number;

    @NotNull
    @Min(1)
    private Integer capacity;

    private Boolean isActive;
}
