package com.elias.GestoBar.repository;

import com.elias.GestoBar.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByNameContainingIgnoreCase(String name);

    List<Category> findByCategoryId(Integer CategoryId);

}