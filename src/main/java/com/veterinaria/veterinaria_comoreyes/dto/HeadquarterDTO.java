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
public class HeadquarterDTO {

    private long headquarterId;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String address;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Size(max = 150, message = "El correo no puede tener más de 150 caracteres")
    private String email;

    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 50, message = "El distrito no puede tener más de 50 caracteres")
    private String district;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 50, message = "La provincia no puede tener más de 50 caracteres")
    private String province;

    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 50, message = "El departamento no puede tener más de 50 caracteres")
    private String department;

    @NotNull(message = "El estado es obligatorio")
    @Min(value = 0, message = "El estado debe ser 0 o 1")
    @Max(value = 1, message = "El estado debe ser 0 o 1")
    private Integer status;
}
