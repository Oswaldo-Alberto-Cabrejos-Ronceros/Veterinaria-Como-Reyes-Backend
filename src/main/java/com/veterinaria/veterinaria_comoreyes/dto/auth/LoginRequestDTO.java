package com.veterinaria.veterinaria_comoreyes.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record LoginRequestDTO (
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Formato de correo inválido")
        @Size(max = 100, message = "El correo no puede tener más de 100 caracteres")
        @Pattern(regexp = "^[^<>\"'\\\\/()]*$", message = "El correo electrónico contiene caracteres no permitidos")
        String email,

                @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=!_.-]*$", message = "La contraseña contiene caracteres inválidos")
        String password
) { }
