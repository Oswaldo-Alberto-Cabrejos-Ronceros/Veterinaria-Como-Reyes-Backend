package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Category;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class,
        componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO maptoCategoryDTO(Category category);

    Category maptoCategory(CategoryDTO categoryDTO);
}
