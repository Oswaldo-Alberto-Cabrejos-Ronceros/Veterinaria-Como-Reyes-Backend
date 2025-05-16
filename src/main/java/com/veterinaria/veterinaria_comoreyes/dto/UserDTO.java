package com.veterinaria.veterinaria_comoreyes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private long userId;

    @NotBlank(message = "El tipo de usuario es obligatorio")
    @Size(max = 30, message = "El tipo no puede tener más de 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "El tipo solo puede contener letras, números y espacios")
    private String type;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 100, message = "El correo no puede tener más de 100 caracteres")
    @Pattern(regexp = "^[^<>\"'\\\\/()]*$", message = "El correo electrónico contiene caracteres no permitidos")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=!_.-]*$", message = "La contraseña contiene caracteres inválidos")
    private String password;

}
