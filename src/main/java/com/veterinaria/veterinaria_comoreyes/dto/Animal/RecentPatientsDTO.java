package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentPatientsDTO {
    private Long animalId;
    private String animalName;
    private String breedName;
    private String clientFullName;
    private String lastVisitDate;      // Formato: "dd/MM/yyyy"
    private BigDecimal animalWeight;
    private String animalSex;
    private String animalBirthDate;    // Formato: "dd/MM/yyyy"
}
