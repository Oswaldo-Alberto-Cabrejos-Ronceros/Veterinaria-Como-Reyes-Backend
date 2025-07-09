package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareResponseDTO {
    private Long careId;
    private String animalName;
    private String breedName;
    private String clientFullName;
    private String serviceName;
    private String careDate;  // Formato: "dd/MM/yyyy"
    private String status;  // Formato: "EN_CURSO", "COMPLETADO", "OBSERVACION"
}
