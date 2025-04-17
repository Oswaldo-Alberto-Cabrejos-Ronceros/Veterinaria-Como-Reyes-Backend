package com.veterinaria.veterinaria_comoreyes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecieDTO {
    private long specieId;
    private String name;
    private String imagePath;
    private Integer status;
}
