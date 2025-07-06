package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentInfoPanelAdminDTO {
    private Long appointmentId;
    private String animalName;
    private String serviceName;
    private String clientName; // Primer nombre + primer apellido
    private String hour;       // Formato HH:mm
    private String status;
}