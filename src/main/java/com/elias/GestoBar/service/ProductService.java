package com.elias.GestoBar.service;

import com.elias.GestoBar.dto.productDTO.ProductRequestDTO;
import com.elias.GestoBar.dto.productDTO.ProductResponseDTO;
import com.elias.GestoBar.exception.ResourceNotFoundException;
import com.elias.GestoBar.mapper.ProductMapper;
import com.elias.GestoBar.model.Category;
import com.elias.GestoBar.model.Product;
import com.elias.GestoBar.repository.CategoryRepository;
import com.elias.GestoBar.repository.ProductRepository;
import com.elias.GestoBar.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Category category = findCategoryOrThrow(dto.getCategoryId());

        Product product = productMapper.toEntity(dto);
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }



    public ProductResponseDTO getProductById(Integer productId) {
        return productMapper.toResponse(findProductOrThrow(productId));
    }


    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    public List<ProductResponseDTO> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    public List<ProductResponseDTO> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    public List<ProductResponseDTO> getProductsBySellPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findBySellPriceBetween(min, max)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductResponseDTO> getActiveProducts() {
        return productRepository.findByIsActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductResponseDTO> getInactiveProducts() {
        return productRepository.findByIsActiveFalse()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    @Transactional
    public ProductResponseDTO updateProduct(Integer productId, ProductRequestDTO dto) {
        Product existing = findProductOrThrow(productId);
        Category category = findCategoryOrThrow(dto.getCategoryId());

        productMapper.updateEntityFromDTO(dto, existing);
        existing.setCategory(category);

        return productMapper.toResponse(productRepository.save(existing));
    }



    @Transactional
    public void deleteProduct(Integer productId) {
        Product product = findProductOrThrow(productId);
        productRepository.delete(product);
    }


    private Product findProductOrThrow(Integer productId) {
        Product product = productRepository.findByProductId(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return product;
    }

    private Category findCategoryOrThrow(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));
    }
}