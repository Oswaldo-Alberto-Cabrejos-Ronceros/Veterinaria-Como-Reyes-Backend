package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyCareStatsDTO {
    private List<String> weekLabels; // Ej: ["30/07 al 05/08", "23/07 al 29/07", ...]
    private List<Long> totalCares;   // Total de atenciones por semana
}
