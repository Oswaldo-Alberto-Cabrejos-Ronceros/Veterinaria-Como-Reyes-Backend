package com.veterinaria.veterinaria_comoreyes.dto.Specie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopSpeciesByAppointmentsDTO {
    private List<String> speciesNames;
    private List<Long> appointmentCounts;
}
