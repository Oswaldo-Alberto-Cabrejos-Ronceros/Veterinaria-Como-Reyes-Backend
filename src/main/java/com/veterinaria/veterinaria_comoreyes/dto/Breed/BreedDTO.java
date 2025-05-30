package com.veterinaria.veterinaria_comoreyes.dto.Breed;

import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreedDTO {

    private Long breedId;

    @NotNull
    private Specie specie;

    @NotBlank(message = "El nombre de la raza es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;
}
