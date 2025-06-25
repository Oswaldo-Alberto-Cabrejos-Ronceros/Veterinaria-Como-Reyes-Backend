package com.veterinaria.veterinaria_comoreyes.dto.Employee;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnimalInfoForClientDTO {
    private Long animalId;
    private String birthDate;
    private String gender;
    private String name;
    private String urlImage;
    private Double weight;
    private String breedName;
    private String speciesName;

}
