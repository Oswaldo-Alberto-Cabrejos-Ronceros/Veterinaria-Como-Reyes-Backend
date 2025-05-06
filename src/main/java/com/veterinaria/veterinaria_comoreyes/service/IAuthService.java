package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginResponseDTO;

public interface IAuthService {
    LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest);

    LoginResponseDTO authenticateClient(LoginRequestDTO loginRequest);
}
