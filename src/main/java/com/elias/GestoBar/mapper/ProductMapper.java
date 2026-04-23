package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.productDTO.ProductRequestDTO;
import com.elias.GestoBar.dto.productDTO.ProductResponseDTO;
import com.elias.GestoBar.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(source = "category.categoryId", target = "categoryId")
    ProductResponseDTO toResponse(Product product);

}
