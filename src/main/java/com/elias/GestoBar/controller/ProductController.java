package com.elias.GestoBar.controller;

import com.elias.GestoBar.dto.productDTO.ProductRequestDTO;
import com.elias.GestoBar.dto.productDTO.ProductResponseDTO;
import com.elias.GestoBar.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //POST /api/v1/products
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(dto));
    }

    //GET /api/v1/products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //GET /api/v1/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    //GET /api/v1/products/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Integer categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    //GET /api/v1/products/search?name=?
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(productService.getProductsByName(name));
    }

    //GET /api/v1/products/price-range?min=?&max=?
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> getByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(productService.getProductsBySellPriceRange(min, max));
    }

    //GET /api/v1/products/active
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }

    //GET /api/v1/products/inactive
    @GetMapping("/inactive")
    public ResponseEntity<List<ProductResponseDTO>> getInactiveProducts() {
        return ResponseEntity.ok(productService.getInactiveProducts());
    }

    //PUT /api/v1/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    //DELETE /api/v1/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
