package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PaymentMethodDetailDTO {
    private String methodName;
    private Double totalAmount;
    private Long count;
}
