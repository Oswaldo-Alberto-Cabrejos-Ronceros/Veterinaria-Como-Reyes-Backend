package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    private String scheduleDateTime;

    private String statusAppointment;

    private Long headquarterVetServiceId;

    private Long employeeId;

    private Long animalId;
}
