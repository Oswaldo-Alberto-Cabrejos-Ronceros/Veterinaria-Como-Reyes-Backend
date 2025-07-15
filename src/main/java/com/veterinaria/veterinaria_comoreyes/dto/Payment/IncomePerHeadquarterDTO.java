package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IncomePerHeadquarterDTO{
    private List<String> headquarterLabels;
    private List<Double> totalIncomes;
}

