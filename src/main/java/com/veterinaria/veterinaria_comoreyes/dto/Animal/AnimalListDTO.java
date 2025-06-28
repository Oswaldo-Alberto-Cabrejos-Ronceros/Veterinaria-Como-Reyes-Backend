package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalListDTO {
    private Long animalId;
    private String name;
    private String gender;
    private Long breedId;
    private Long clientId;
    private String status;
}
