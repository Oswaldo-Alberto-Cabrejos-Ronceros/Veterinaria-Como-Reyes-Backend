package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularServicesDTO {
    private String serviceName;
    private String image;
    private Long count;

    // Constructor requerido para Spring Data JPA native query projection
    public PopularServicesDTO(String serviceName, String image, Number count) {
        this.serviceName = serviceName;
        this.image = image;
        this.count = count != null ? count.longValue() : 0L;
    }
}