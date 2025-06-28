package com.veterinaria.veterinaria_comoreyes.dto.Specie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecieListDTO {

    private Long specieId;

    private String name;

    private String imagePath;

    private Boolean status;
}
