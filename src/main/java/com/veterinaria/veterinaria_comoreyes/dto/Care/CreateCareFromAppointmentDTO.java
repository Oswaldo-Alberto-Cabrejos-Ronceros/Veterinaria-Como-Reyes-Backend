package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCareFromAppointmentDTO {
    private Long appointmentId;
    private Long employeeId;
}
