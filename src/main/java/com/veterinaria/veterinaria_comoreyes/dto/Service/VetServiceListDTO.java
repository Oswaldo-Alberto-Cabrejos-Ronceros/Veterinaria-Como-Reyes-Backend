package com.veterinaria.veterinaria_comoreyes.dto.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VetServiceListDTO {
    private Long serviceId;

    private String name;

    private Double price;

    private String specieId;

    private String categoryId;

    private String status;
}
