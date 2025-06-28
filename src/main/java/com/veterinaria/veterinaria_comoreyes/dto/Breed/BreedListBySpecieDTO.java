package com.veterinaria.veterinaria_comoreyes.dto.Breed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BreedListBySpecieDTO {
    private Long breedId;
    private String specieName;
    private String breedName;
    private String status;
}
