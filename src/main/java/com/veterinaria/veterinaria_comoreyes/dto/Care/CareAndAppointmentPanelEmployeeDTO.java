package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareAndAppointmentPanelEmployeeDTO {

    private Long id;
    private String type; // "CITA" o "ATENCIÓN"
    private String animalName;
    private String serviceName;
    private String clientName;
    private String date; // yyyy-MM-dd
    private String hour; // HH:mm
    private String status;
}
