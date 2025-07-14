package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopPaymentMethodsDTO {
    private List<String> methodLabels; // Ej: ["Efectivo", "Yape", "Tarjeta", "Plin", "Otros"]
    private List<Long> totalPayments;  // Ej: [40, 30, 25, 20, 12]
}
