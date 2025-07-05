package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalWeightUpdateDTO {
    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", inclusive = true, message = "El peso debe ser mayor a 0")
    @Digits(integer = 5, fraction = 2, message = "El peso debe tener máximo 2 decimales")
    private Float weight;
}
