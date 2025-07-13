package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CaresByVetAndHeadquarterDTO {
    private String headquarterName;
    private String vetName;
    private BigDecimal completedCares;  // Cambiado de Long a Integer
}