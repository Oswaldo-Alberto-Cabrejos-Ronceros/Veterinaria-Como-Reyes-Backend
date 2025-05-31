package com.veterinaria.veterinaria_comoreyes.dto.Specie;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecieDTO {

    private Long specieId;

    @NotBlank(message = "El nombre de la especie es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "La ruta de la imagen es obligatoria")
    @Size(max = 255, message = "La ruta de la imagen no puede tener más de 255 caracteres")
    private String imagePath;

}
