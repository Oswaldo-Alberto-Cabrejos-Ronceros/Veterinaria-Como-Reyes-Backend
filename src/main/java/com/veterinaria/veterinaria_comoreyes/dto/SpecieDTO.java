package com.veterinaria.veterinaria_comoreyes.dto;

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

    private long specieId;

    @NotBlank(message = "El nombre de la especie es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "La ruta de la imagen es obligatoria")
    @Size(max = 255, message = "La ruta de la imagen no puede tener más de 255 caracteres")
    private String imagePath;

    @NotNull(message = "El estado es obligatorio")
    @Min(value = 0, message = "El estado debe ser 0 o 1")
    @Max(value = 1, message = "El estado debe ser 0 o 1")
    private Integer status;
}
