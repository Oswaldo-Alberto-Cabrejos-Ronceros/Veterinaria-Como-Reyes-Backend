package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private long categoryId;
    private String name;
    private String description;
    private Integer status;
}
