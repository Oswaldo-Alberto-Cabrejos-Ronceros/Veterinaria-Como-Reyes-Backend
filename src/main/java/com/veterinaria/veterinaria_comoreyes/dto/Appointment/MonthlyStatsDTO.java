package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyStatsDTO{
    private Double totalIncomeThisMonth;
    private Long totalCompletedCaresThisMonth;
    private Long totalCompletedAppointmentsThisMonth;
    private Double appointmentSuccessRate; // en porcentaje
}
