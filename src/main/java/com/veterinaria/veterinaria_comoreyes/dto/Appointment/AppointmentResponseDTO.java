package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service.HeadquarterVetServiceDTO;
import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {

    private Long appointmentId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduleDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;

    private String comment;

    private String cancellationNote;

    private StatusAppointment statusAppointment;

    private HeadquarterVetServiceDTO headquarterVetService;

    private EmployeeDTO assignedEmployee;

    private AnimalDTO animal;

}

