package com.veterinaria.veterinaria_comoreyes.dto.Specie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopSpeciesCareDTO {
    private List<String> speciesLabels;   // Ej: ["Perro", "Gato", "Conejo", "Ave", "Otros"]
    private List<Long> totalCounts;       // Ej: [40, 25, 10, 5, 8]
}
