package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.restaurantTableDTO.RestaurantTableRequestDTO;
import com.elias.GestoBar.dto.restaurantTableDTO.RestaurantTableResponseDTO;
import com.elias.GestoBar.model.RestaurantTable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantTableMapper {
    RestaurantTable toEntity(RestaurantTableRequestDTO restaurantTableRequestDTO);
    RestaurantTableResponseDTO toResponse(RestaurantTable restaurantTable);
}
