package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationalMonthlyStatsDTO {
    private Long totalPatients;
    private Long totalClients;
    private Long activeVeterinarians;
    private Double avgIncomePerVet;
}
