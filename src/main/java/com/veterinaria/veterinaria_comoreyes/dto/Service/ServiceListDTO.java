package com.veterinaria.veterinaria_comoreyes.dto.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceListDTO {
    private Long serviceId;
    private String name;
    private String specie;
    private String category;
    private String status;
}
