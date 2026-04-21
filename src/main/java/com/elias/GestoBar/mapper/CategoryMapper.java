package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.categoryDTO.CategoryRequestDTO;
import com.elias.GestoBar.dto.categoryDTO.CategoryResponseDTO;
import com.elias.GestoBar.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO toResponse(Category category);

}
