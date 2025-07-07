package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoAppointmentForPanelDTO {
    private Long idAppointment;       // appointment.appointment_id
    private String timeAppointment;   // TO_CHAR(appointment.schedule_date_time, 'HH24:MI')
    private String comment;           // appointment.comment
    private Long serviceId;           // veterinary_service.service_id
    private int serviceTime;          // veterinary_service.duration
    private String serviceName;       // veterinary_service.name
    private Long employeeId;        // employee.employee_id
    private String employeeName;      // empleado formateado
    private String employeeRole;      // VETERINARIO, PRACTICANTE, ASISTENTE o null

}
