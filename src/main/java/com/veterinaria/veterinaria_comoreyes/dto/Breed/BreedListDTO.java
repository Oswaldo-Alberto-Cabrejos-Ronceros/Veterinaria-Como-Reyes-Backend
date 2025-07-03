package com.veterinaria.veterinaria_comoreyes.dto.Breed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BreedListDTO {
    private Long breedId;
    private String name;
    private String specieName;
    private String status;
}
