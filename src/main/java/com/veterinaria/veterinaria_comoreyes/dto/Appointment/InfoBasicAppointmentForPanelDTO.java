package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoBasicAppointmentForPanelDTO {

    Long id;

    @JsonFormat( shape = JsonFormat.Shape.STRING)
    String date;

    @JsonFormat( shape = JsonFormat.Shape.STRING)
    String time;

    String animalName;

    String serviceName;

    String serviceDescription;

    String serviceImage;

    String categoryServiceName;

    String status;

    Integer duration; // Duration in minutes

}
