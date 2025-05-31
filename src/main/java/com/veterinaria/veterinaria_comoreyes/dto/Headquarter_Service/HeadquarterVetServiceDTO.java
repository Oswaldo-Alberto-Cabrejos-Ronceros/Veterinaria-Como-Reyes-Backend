package com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service;

import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HeadquarterVetServiceDTO {

    private Long id;

    @NotNull(message = "La sede no puede ser nula")
    private Long headquarterId;

    @NotNull(message = "El servicio veterinario no puede ser nulo")
    private VeterinaryServiceDTO service;
}
