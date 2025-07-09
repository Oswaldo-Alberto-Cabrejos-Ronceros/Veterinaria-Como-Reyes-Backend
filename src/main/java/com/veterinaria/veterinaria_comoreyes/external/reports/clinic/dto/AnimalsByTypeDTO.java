package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalsByTypeDTO {
    private String type;
    private String name;
    private String image;
    private Long count;

    // Constructor requerido para Spring Data JPA native query projection
    public AnimalsByTypeDTO(String type, String name, String image, Number count) {
        this.type = type;
        this.name = name;
        this.image = image;
        this.count = count != null ? count.longValue() : 0L;
    }
}