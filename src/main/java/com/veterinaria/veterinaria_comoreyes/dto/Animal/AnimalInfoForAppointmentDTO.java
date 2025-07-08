package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalInfoForAppointmentDTO {
    private Long animalId;        // row[0]
    private String birthDate;     // row[1]
    private String name;          // row[2]
    private String urlImage;      // row[3]
    private BigDecimal weight;    // row[4]
    private String breedName;     // row[5]
    private String speciesName;   // row[6]
    private String animalComment; // row[7]
}
