package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VeterinarianPerformanceDTO {
    private List<String> employeesNames;
    private List<Long> totalPatients;
    private List<Long> totalAppointments;

}
