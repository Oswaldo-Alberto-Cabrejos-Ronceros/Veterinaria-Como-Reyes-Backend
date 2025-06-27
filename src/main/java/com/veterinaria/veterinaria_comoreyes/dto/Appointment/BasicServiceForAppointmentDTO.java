package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BasicServiceForAppointmentDTO {

    private Long headquarterServiceId;
    private Long serviceId;
    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    private Integer duration;
    private String specieName;
    private String serviceImageUrl;
    private String categoryName;

}
