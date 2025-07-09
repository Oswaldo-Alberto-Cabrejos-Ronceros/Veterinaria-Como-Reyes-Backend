package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyIncomeDTO {
    private List<String> days;      // ["Sunday", "Monday", ...]
    private List<BigDecimal> totals; // [123.00, 456.50, ...]
}
