package com.veterinaria.veterinaria_comoreyes.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @Valid
    private UserDTO user;

    @Valid
    private ClientDTO client;
}
