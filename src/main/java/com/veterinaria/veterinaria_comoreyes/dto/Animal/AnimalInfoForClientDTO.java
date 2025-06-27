package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalInfoForClientDTO {
    private Long animalId;        // row[0]

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;     // row[1]

    private String gender;        // row[2]
    private String name;          // row[3]
    private String urlImage;      // row[4]
    private BigDecimal weight;    // row[5]
    private Long speciesId;       // row[6]
    private String breedName;     // row[7]
    private String speciesName;   // row[8]
    private String animalComment; // row[9]
}
