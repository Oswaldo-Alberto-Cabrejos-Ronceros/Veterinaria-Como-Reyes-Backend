package com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadquarterServiceInfoPanelDTO {
    private Long headquarterId;
    private String headquarterName;
    private Long ServiceId;
    private String serviceName;
    private String serviceDescription;
    private String servicePrice;
    private String serviceDuration;
    private String categoryName;
    private String speciesName;
}
