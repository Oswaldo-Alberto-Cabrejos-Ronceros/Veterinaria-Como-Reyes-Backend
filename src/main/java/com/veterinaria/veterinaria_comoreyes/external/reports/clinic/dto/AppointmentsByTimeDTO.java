package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto;

public class AppointmentsByTimeDTO {
    private String period;
    private Long count;

    public AppointmentsByTimeDTO(String period, Number count) {
        this.period = period;
        this.count = count != null ? count.longValue() : 0L;
    }

    public String getPeriod() {
        return period;
    }

    public Long getCount() {
        return count;
    }
}
