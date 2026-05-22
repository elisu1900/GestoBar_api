package com.elias.GestoBar.service;

import com.elias.GestoBar.dto.categoryDTO.CategoryRequestDTO;
import com.elias.GestoBar.dto.categoryDTO.CategoryResponseDTO;
import com.elias.GestoBar.exception.ResourceNotFoundException;
import com.elias.GestoBar.mapper.CategoryMapper;
import com.elias.GestoBar.model.Category;
import com.elias.GestoBar.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    public CategoryResponseDTO getCategoryById(Integer categoryId) {
        return categoryMapper.toResponse(findOrThrow(categoryId));
    }

    public List<CategoryResponseDTO> getAllCategories() {
        List<CategoryResponseDTO> cats = new ArrayList<>();
        for (Category c : categoryRepository.findAll()) {
            cats.add(categoryMapper.toResponse(c));
        }
        return cats;
    }

    public List<CategoryResponseDTO> searchByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Integer categoryId, CategoryRequestDTO dto) {
        Category existing = findOrThrow(categoryId);
        existing.setName(dto.getName());
        return categoryMapper.toResponse(categoryRepository.save(existing));
    }

    @Transactional
    public void deleteCategory(Integer categoryId) {
        categoryRepository.delete(findOrThrow(categoryId));
    }

    private Category findOrThrow(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));
    }
}
