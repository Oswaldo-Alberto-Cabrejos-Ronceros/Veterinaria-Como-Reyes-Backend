package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class InfoBasicAppointmentForPanelDTO {
    Long id;

    @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)

    LocalDate date;
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    String time;

    String petName;

    String serviceName;

    String serviceImage;

    String categoryServiceName;
}
