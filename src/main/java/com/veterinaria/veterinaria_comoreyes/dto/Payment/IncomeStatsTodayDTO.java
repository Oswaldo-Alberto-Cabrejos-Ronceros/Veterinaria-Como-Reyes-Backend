package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeStatsTodayDTO {
    private BigDecimal todayIncome;
}
