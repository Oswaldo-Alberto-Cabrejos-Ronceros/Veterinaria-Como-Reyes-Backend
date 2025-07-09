package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatsForReceptionistDTO {
    private Integer totalAppointmentsToday;
    private Integer confirmedAppointmentsToday;
    private Integer pendingAppointmentsToday;
}
