package com.veterinaria.veterinaria_comoreyes.dto.Animal;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para animales")
public class AnimalDTO {

    private Long animalId;

    @NotBlank(message = "El nombre de la raza es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "El genero es obligatorio")
    @Size(min=1, max = 1,message = "El genero solo tiene un digito")
    private String gender;

    @NotNull(message = "El peso es obligatorio")
    @Digits(integer = 3,fraction = 2, message = "El peso debe tener como maximo 2 digitos")
    private float weight;

    @Past
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotBlank(message = "La fecha de nacimiento es obligatorio")
    private LocalDate birthDate;

    @Null
    @Size(max=255, message = "El comentario solo debe tener menos de 255 digitos")
    private String animalComment;

    @NotBlank
    private String urlImage;

    @NotNull
    private Breed breed;

    @NotNull
    private Long clientId;

}
