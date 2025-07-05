package com.veterinaria.veterinaria_comoreyes.dto.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesInfoTopPanelAdminDTO {
    private Long serviceId;
    private String serviceName;
    private String categoryName;
    private Long totalCares;
}
