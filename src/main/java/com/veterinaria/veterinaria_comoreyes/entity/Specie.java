package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long specieId;
    private String name;
    private String imagePath;
    private Integer status;
}
