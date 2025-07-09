package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentsByVetDTO {
    private String vetName;
    private Long totalAppointments;
    private Long completed;
    private Long cancelled;

    public AppointmentsByVetDTO(String vetName, Number totalAppointments, Number completed, Number cancelled) {
        this.vetName = vetName;
        this.totalAppointments = totalAppointments != null ? totalAppointments.longValue() : 0L;
        this.completed = completed != null ? completed.longValue() : 0L;
        this.cancelled = cancelled != null ? cancelled.longValue() : 0L;
    }
}
