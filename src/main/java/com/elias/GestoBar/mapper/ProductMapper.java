package com.elias.GestoBar.mapper;

import com.elias.GestoBar.dto.productDTO.ProductRequestDTO;
import com.elias.GestoBar.dto.productDTO.ProductResponseDTO;
import com.elias.GestoBar.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.name",       target = "categoryName")
    @Mapping(source = "sellPrice",           target = "sellPrice")
    @Mapping(source = "costPrice",           target = "costPrice")
    ProductResponseDTO toResponse(Product product);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "category",  ignore = true)
    void updateEntityFromDTO(ProductRequestDTO dto, @MappingTarget Product product);
}