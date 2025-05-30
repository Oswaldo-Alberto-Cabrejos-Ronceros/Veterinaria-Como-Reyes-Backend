package com.veterinaria.veterinaria_comoreyes.dto.Headquarter;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 HeadquarterDTO of Headquarter(entity):
 To add or update the entire entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterDTO {

    private Long headquarterId;

    @NotBlank(message = "El nombre clave de la sede es obligatoria")
    @Size(max = 40, message = "El nombre de la sede no puede tener más de 40 caracteres")
    private  String name;

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

}
