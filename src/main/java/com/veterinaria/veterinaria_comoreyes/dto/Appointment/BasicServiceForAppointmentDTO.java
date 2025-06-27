package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BasicServiceForAppointmentDTO {

    private Long headquarterServiceId;
    private Long serviceId;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
}
