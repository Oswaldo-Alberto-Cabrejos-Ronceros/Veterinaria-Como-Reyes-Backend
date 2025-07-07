package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AppointmentNotificationDTO {
    private Long appointmentId;
    private String ownerName;
    private LocalDateTime scheduleDateTime;
    private String formattedDate; // Nuevo campo para fecha formateada
    private String formattedTime; // Nuevo campo para hora formateada
    private String headquarterName; // Corregido: de headquarterNane a headquarterName
    private String vetServiceName;
    private String petName;
    private String ownerEmail; // Nuevo campo para el email del cliente
}