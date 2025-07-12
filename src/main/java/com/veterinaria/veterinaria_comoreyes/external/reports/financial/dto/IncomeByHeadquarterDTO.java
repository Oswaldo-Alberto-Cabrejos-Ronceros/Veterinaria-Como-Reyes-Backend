package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeByHeadquarterDTO {
    private String headquarterName;
    private Double totalIncome;
    private Long totalPayments;
    private Double averageIncome;
    private String mostUsedPaymentMethod;

    // Constructor para la consulta básica
    public IncomeByHeadquarterDTO(String headquarterName, Double totalIncome, Long totalPayments) {
        this.headquarterName = headquarterName;
        this.totalIncome = totalIncome;
        this.totalPayments = totalPayments;
        this.averageIncome = totalPayments > 0 ? totalIncome / totalPayments : 0.0;
    }
}