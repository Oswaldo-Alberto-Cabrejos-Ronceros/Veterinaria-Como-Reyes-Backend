package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualRevenueDTO {
    private List<String> monthLabels;   // Ej: ["ENE","FEB",…,"JUL"]
    private List<Double> totalIncomes;  // Ej: [1200.0, 1500.0,…, 980.0]
}
