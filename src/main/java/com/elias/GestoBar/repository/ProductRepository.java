package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByProductId(Integer productId);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findBySellPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByCostPriceBetween(BigDecimal min, BigDecimal max);


    List<Product> findByIsActiveTrue();

    List<Product> findByIsActiveFalse();

    List<Product> findByCategory_CategoryId(Integer categoryId);

}
