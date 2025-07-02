package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<CategoryListDTO> searchCategories(String name, Boolean status, Pageable pageable);
}
