package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentsByVetAndPeriodDTO {
    private String vetName;
    private String period; // puede ser día, semana, mes o año
    private Long completedAppointments;

    // Constructor necesario para native queries
    public AppointmentsByVetAndPeriodDTO(String vetName, String period, Number completedAppointments) {
        this.vetName = vetName;
        this.period = period;
        this.completedAppointments = completedAppointments != null ? completedAppointments.longValue() : 0L;
    }
}