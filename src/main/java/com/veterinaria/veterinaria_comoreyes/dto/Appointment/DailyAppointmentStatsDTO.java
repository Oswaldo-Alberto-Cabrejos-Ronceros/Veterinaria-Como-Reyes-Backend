package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DailyAppointmentStatsDTO{
    private List<String> dayLabels;         // Ejemplo: ["LUN", "MAR", "MIÉ", ...]
    private List<Long> completedCounts;     // Ej: [4, 6, 3, 5, 2, 0, 7]
    private List<Long> cancelledCounts;     // Ej: [1, 0, 2, 0, 0, 1, 1]
}
