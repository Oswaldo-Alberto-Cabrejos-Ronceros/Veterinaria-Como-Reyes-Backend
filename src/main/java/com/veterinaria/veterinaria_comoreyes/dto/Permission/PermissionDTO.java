package com.veterinaria.veterinaria_comoreyes.dto.Permission;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PermissionDTO {
    private Long permissionId;

    @NotBlank(message = "El código de acción es obligatorio")
    @Size(max = 50, message = "El código de acción no puede exceder los 50 caracteres")
    private String actionCode;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @Size(max = 200, message = "La descripción no puede exceder los 200 caracteres")
    private String description;

    @NotBlank(message = "El módulo es obligatorio")
    @Size(max = 30, message = "El módulo no puede exceder los 30 caracteres")
    private String module;

    private Boolean status;

    private Set<Long> roleIds;
}
