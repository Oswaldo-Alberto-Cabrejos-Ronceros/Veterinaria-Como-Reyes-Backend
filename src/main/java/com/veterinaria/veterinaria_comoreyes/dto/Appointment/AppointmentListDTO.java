package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AppointmentListDTO {

    private Long appointmentId;
    private String petName;
    private String petOwner;
    private String day; // scheduleDateTime (solo el dia)
    private String headquarter; // headquarterVetService (dentro esta el headquarter)
    private String categoryService;
    private StatusAppointment appointmentStatus;

    public AppointmentListDTO(
            Long appointmentId,
            String petName,
            String petOwner,
            String day,
            String headquarter,
            String categoryService,
            StatusAppointment appointmentStatus) {
        this.appointmentId = appointmentId;
        this.petName = petName;
        this.petOwner = petOwner;
        this.day = day;
        this.headquarter = headquarter;
        this.categoryService = categoryService;
        this.appointmentStatus = appointmentStatus;
    }

}
