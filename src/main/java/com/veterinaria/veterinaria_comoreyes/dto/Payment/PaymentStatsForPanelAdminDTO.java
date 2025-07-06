package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatsForPanelAdminDTO {
    private double currentTotal;    // Total de pages del mes actual
    private double previousTotal;   // Total de pages del mes anterior
    private String percentageDifference;
}
