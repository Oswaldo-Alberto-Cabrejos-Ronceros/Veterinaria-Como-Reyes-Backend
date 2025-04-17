package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Category;
import com.veterinaria.veterinaria_comoreyes.mapper.CategoryMapper;
import com.veterinaria.veterinaria_comoreyes.repository.CategoryRepository;
import com.veterinaria.veterinaria_comoreyes.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return CategoryMapper.maptoCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::maptoCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.maptoCategory(categoryDTO);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.maptoCategoryDTO(saved);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existing.setName(categoryDTO.getName());
        existing.setDescription(categoryDTO.getDescription());
        existing.setStatus(categoryDTO.getStatus());

        Category updated = categoryRepository.save(existing);
        return CategoryMapper.maptoCategoryDTO(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setStatus(0); // Inactivo
        categoryRepository.save(category);
    }
}
