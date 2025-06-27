package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import java.time.LocalDateTime;

import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentListDTO {

    private Long appointmentId;

    private LocalDateTime scheduleDateTime;

    private StatusAppointment statusAppointment;

    private Long headquarterVetServiceId;

    private Long employeeId;

    private Long animalId;
}
