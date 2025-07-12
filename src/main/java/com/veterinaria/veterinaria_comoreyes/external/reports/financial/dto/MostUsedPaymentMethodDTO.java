package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MostUsedPaymentMethodDTO {
    private String methodName;
    private Long usageCount;

    // Opcional: si aún necesitas este getter personalizado
    public String getMethodName() {
        return methodName;
    }
}
