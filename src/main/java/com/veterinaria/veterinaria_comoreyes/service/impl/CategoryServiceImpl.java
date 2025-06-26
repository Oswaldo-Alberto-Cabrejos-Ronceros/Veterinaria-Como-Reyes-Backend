package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryDTO;
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

    private final CategoryRepository categoryRepository;
    private final FilterStatus filterStatus;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FilterStatus filterStatus, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.filterStatus = filterStatus;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDTO getCategoryById(Long id) {
        filterStatus.activeFilterStatus(true);
        Category category = categoryRepository.findByCategoryIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.maptoCategoryDTO(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> getAllCategories() {
        filterStatus.activeFilterStatus(true);
        return categoryRepository.findAll().stream()
                .filter(Category::getStatus)
                .map(categoryMapper::maptoCategoryDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.maptoCategory(categoryDTO);
        category.setStatus(true);
        return categoryMapper.maptoCategoryDTO(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        filterStatus.activeFilterStatus(true);
        Category existing = categoryRepository.findByCategoryIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existing.setName(categoryDTO.getName());
        existing.setDescription(categoryDTO.getDescription());

        return categoryMapper.maptoCategoryDTO(categoryRepository.save(existing));
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        filterStatus.activeFilterStatus(true);
        Category category = categoryRepository.findByCategoryIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setStatus(false); // Inactivo
        categoryRepository.save(category);
    }
}
