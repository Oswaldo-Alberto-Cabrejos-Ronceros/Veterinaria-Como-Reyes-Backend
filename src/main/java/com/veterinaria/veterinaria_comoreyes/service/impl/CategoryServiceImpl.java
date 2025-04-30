package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Category;
import com.veterinaria.veterinaria_comoreyes.mapper.CategoryMapper;
import com.veterinaria.veterinaria_comoreyes.repository.CategoryRepository;
import com.veterinaria.veterinaria_comoreyes.service.ICategoryService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {


    private CategoryRepository categoryRepository;
    private FilterStatus filterStatus;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FilterStatus filterStatus){
        this.categoryRepository = categoryRepository;
        this.filterStatus = filterStatus;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDTO getCategoryById(Long id) {
        filterStatus.activeFilterStatus(true);
        Category category = categoryRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return CategoryMapper.maptoCategoryDTO(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> getAllCategories() {
        filterStatus.activeFilterStatus(true);
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::maptoCategoryDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        filterStatus.activeFilterStatus(true);
        Category category = CategoryMapper.maptoCategory(categoryDTO);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.maptoCategoryDTO(saved);
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        filterStatus.activeFilterStatus(true);
        Category existing = categoryRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existing.setName(categoryDTO.getName());
        existing.setDescription(categoryDTO.getDescription());

        Category updated = categoryRepository.save(existing);
        return CategoryMapper.maptoCategoryDTO(updated);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        filterStatus.activeFilterStatus(true);
        Category category = categoryRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setStatus(false); // Inactivo
        categoryRepository.save(category);
    }
}
