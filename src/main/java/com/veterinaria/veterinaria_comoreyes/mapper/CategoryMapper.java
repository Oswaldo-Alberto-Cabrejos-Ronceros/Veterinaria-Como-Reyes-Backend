package com.veterinaria.veterinaria_comoreyes.mapper;


import com.veterinaria.veterinaria_comoreyes.dto.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Category;

public class CategoryMapper {

    public static CategoryDTO maptoCategoryDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getDescription(),
                category.getStatus()
        );
    }

    public static Category maptoCategory(CategoryDTO categoryDTO) {
        return new Category(
                categoryDTO.getCategoryId(),
                categoryDTO.getName(),
                categoryDTO.getDescription(),
                categoryDTO.getStatus()
        );
    }
}
