package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareListDTO {

    private Long careId;
    private String careDateTime;
    private String statusCare;

    private String animalName;
    private String animalSpecies;
    private String animalBreed;

    private String employeeFullName;

    private String serviceName;
    private Double servicePrice;

    private String headquarterName;

    private Long appointmentId;
}
