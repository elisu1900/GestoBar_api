package com.elias.GestoBar.service;

import com.elias.GestoBar.dto.productDTO.ProductRequestDTO;
import com.elias.GestoBar.dto.productDTO.ProductResponseDTO;
import com.elias.GestoBar.exception.ResourceNotFoundException;
import com.elias.GestoBar.mapper.ProductMapper;
import com.elias.GestoBar.model.Category;
import com.elias.GestoBar.model.Product;
import com.elias.GestoBar.repository.CategoryRepository;
import com.elias.GestoBar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        List<ProductResponseDTO> result = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            result.add(productMapper.toResponse(p));
        }
        return result;
    }


    public List<ProductResponseDTO> getProductsByCategory(Integer categoryId) {
        List<Product> products = productRepository.findByCategory_CategoryId(categoryId);
        List<ProductResponseDTO> result = new ArrayList<>();
        for (Product p : products) {
            result.add(productMapper.toResponse(p));
        }
        return result;
    }


    public List<ProductResponseDTO> getProductsByName(String name) {
        List<ProductResponseDTO> result = new ArrayList<>();
        for (Product p : productRepository.findByNameContainingIgnoreCase(name)) {
            result.add(productMapper.toResponse(p));
        }
        return result;
    }


    public List<ProductResponseDTO> getProductsBySellPriceRange(BigDecimal min, BigDecimal max) {
        List<ProductResponseDTO> result = new ArrayList<>();
        for (Product p : productRepository.findBySellPriceBetween(min, max)) {
            result.add(productMapper.toResponse(p));
        }
        return result;
    }

    public List<ProductResponseDTO> getActiveProducts() {
        List<ProductResponseDTO> list = new ArrayList<>();
        for (Product prod : productRepository.findByIsActiveTrue()) {
            list.add(productMapper.toResponse(prod));
        }
        return list;
    }

    public List<ProductResponseDTO> getInactiveProducts() {
        List<ProductResponseDTO> result = new ArrayList<>();
        for (Product p : productRepository.findByIsActiveFalse()) {
            result.add(productMapper.toResponse(p));
        }
        return result;
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
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private Category findCategoryOrThrow(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));
    }
}