package com.veterinaria.veterinaria_comoreyes.dto.Service;

import com.veterinaria.veterinaria_comoreyes.entity.Category;
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
public class VeterinaryServiceDTO {

    private Long serviceId;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;

    @NotNull
    @Digits(integer = 6, fraction = 2, message = "El precio debe tener como maximo 2 digitos")
    private float price;


    private Integer duration;

    private Long simultaneousCapacity;

    @NotNull
    private String dirImage;

    @NotNull
    private Specie specie;

    @NotNull
    private Category category;

}
