package com.veterinaria.veterinaria_comoreyes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PermissionDTO {

    private long permissionId;

    @NotBlank(message = "El código de acción es obligatorio")
    @Size(max = 50, message = "El código de acción no puede tener más de 50 caracteres")
    private String actionCode;

    @NotBlank(message = "El módulo es obligatorio")
    @Size(max = 50, message = "El nombre del módulo no puede tener más de 50 caracteres")
    private String module;

    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    private String description;
}
