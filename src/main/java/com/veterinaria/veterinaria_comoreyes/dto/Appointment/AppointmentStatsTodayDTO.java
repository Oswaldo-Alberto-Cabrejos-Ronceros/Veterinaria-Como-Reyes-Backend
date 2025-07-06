package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatsTodayDTO {
    private Long totalAppointments;        // Total de citas con fecha de hoy
    private Long todayRegisteredAppointments; // Citas registradas hoy para hoy
}