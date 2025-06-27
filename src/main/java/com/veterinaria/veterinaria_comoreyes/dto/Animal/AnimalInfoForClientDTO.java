package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalInfoForClientDTO {
    private Long animalId;        // row[0]

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String birthDate;     // row[1]

    private String gender;        // row[2]
    private String name;          // row[3]
    private String urlImage;      // row[4]
    private Double weight;        // row[5]
    private String breedName;     // row[6]
    private String speciesName;   // row[7]
    private String animalComment; // row[8]
}
