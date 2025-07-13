package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class IncomeByHeadquarterDTO {
    private String headquarterName;
    private Double totalIncome;
    private Long totalPayments;
    private List<PaymentMethodDetailDTO> paymentMethods; // Nuevo

    public IncomeByHeadquarterDTO(String headquarterName, Double totalIncome, Long totalPayments) {
        this.headquarterName = headquarterName;
        this.totalIncome = totalIncome;
        this.totalPayments = totalPayments;
    }
}
