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
    private Long animalId;
    private String animalName;
    private String serviceName;
    private String clientName;
    private String date; // yyyy-MM-dd
    private String hour; // HH:mm
    private String status;
    private String commentAppointment;
    private Long employeeId;// Comentarios de la cita
    private String employeeName; // Nombre del empleado que atiende
    private String breedName; // Nombre de la raza del animal
}
