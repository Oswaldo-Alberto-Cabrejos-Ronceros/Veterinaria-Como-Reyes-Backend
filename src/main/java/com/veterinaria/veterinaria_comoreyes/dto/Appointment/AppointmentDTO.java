package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private LocalDateTime scheduleDateTime;
    private LocalDateTime creationDate;
    private String comment;
    private String cancellationNote;
    private StatusAppointment statusAppointment;
    private Long employeeId;
    private Long animalId;
    private Long headQuarterServiceId;
    private StatusAppointment statusAppointmentStatus;
}
