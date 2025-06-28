package com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.entity.StatusVeterinaryRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VeterinaryRecordDTO {

    private Long id;

    @NotNull
    private Long careId;      // ID del cuidado relacionado

    private Long employeeId;  // ID del empleado que atendió

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateCreated;

    private String diagnosis;

    private String treatment;

    private String observations;

    private String resultUrl;

    private StatusVeterinaryRecord statusVeterinaryRecord = StatusVeterinaryRecord.EN_CURSO;
}
