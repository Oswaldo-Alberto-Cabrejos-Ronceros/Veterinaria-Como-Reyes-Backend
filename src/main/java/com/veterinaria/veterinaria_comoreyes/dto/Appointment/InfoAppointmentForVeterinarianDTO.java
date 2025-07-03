package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoAppointmentForVeterinarianDTO {
    private Long idAppointment;
    @JsonFormat( shape = JsonFormat.Shape.STRING)
    private String date;
    @JsonFormat( shape = JsonFormat.Shape.STRING)
    private String time;
    private String animalId;
    private String animalName;
    private String specieName;
    private String clientName;
    private String status;
}
