package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AnimalListDTO {
    private Long animalId;
    private String name;
    private String owner;
    private String specie;
    private String breed;
    private String gender;
    private String status;
}
