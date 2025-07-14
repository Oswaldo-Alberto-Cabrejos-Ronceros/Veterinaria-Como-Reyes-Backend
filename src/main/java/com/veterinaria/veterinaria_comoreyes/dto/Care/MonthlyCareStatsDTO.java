package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyCareStatsDTO {
    private String month; // Ej: "Julio 2025"
    private Long totalPatients;
    private Long totalCares;
}
