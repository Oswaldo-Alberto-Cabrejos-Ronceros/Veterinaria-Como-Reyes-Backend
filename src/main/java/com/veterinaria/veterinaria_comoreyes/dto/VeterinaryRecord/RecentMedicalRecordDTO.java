package com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentMedicalRecordDTO {
    private Long veterinaryRecordId;
    private Long careId;
    private String animalName;
    private String breedName;
    private String clientFullName;
    private String serviceName;
    private String recordMedicalDate;  // Formato: "dd/MM/yyyy"
    private String diagnosis;
    private String treatment;
    private String observations;
    private String status;
}
