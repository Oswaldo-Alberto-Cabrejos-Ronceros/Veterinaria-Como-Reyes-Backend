package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryService {
    CategoryDTO getCategoryById(Long id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    @Transactional
    void activateCategory(Long categoryId);
}
