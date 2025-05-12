package com.veterinaria.veterinaria_comoreyes.security.auth.service;

import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginResponseDTO;


public interface IAuthService {
    LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest);

    LoginResponseDTO authenticateClient(LoginRequestDTO loginRequest);
}
