package com.veterinaria.veterinaria_comoreyes.dto.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDTO {
    private Long categoryId;
    private String name;
    private String status;
}
