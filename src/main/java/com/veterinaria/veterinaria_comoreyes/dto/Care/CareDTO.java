package com.veterinaria.veterinaria_comoreyes.dto.Care;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.entity.StatusCare;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareDTO {

    private Long careId;

    @NotNull(message = "Fecha y hora de atención son requeridas")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateTime;

    @NotNull(message = "Status de atención es requerido")
    private StatusCare statusCare= StatusCare.EN_CURSO;


    private Long headquarterVetServiceId;

    private Long appointmentId;

    private Long animalId;

    private Long employeeId;
}
