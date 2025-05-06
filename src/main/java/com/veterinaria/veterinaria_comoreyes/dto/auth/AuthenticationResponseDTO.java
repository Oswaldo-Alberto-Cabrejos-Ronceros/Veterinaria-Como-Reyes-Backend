package com.veterinaria.veterinaria_comoreyes.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private Long usuarioId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "Nombre no puede tener más de 50 caracteres")
    private String name;
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50,message = "El nombre del rol no puede tener más de 50 caracteres")
    private String role;
    private String jwtToken;
    private String refreshToken;
    public AuthenticationResponseDTO(Long usuarioId, String name, String role){
        this.usuarioId=usuarioId;
        this.name=name;
        this.role=role;
    }

}

