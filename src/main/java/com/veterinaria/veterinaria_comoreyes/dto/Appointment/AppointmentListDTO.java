package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentListDTO {

    Long appointmentId;
    String day; //scheduleDateTime (solo el dia)
    String headquarter; // headquarterVetService (dentro esta el headquarter)
    String categoryService;
    String appointmentStatus;
}
