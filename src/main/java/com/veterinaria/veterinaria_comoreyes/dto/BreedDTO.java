package com.veterinaria.veterinaria_comoreyes.dto;

import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreedDTO {

    private long breedId;

    @NotNull
    private Specie specie;

    @NotBlank(message = "El nombre de la raza es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotNull
    @Size(min = 0, max = 1, message = "El estado debe ser 0 o 1")
    private Byte status;
}
