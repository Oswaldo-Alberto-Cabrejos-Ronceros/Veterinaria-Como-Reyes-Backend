package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentListDTO {

    private Long appointmentId;
    private String day; //scheduleDateTime (solo el dia)
    private String headquarter; // headquarterVetService (dentro esta el headquarter)
    private String categoryService;
    private StatusAppointment appointmentStatus;
}
