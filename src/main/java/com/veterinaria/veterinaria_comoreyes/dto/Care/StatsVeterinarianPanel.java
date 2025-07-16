package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsVeterinarianPanel {
    private Long totalCares;
    private Long totalPatients;
    private Long totalRecords;
}
