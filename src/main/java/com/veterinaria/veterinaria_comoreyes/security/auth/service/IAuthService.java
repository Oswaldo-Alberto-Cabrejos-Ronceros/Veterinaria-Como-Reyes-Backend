package com.veterinaria.veterinaria_comoreyes.security.auth.service;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


public interface IAuthService {


    LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest, HttpServletResponse response);

    LoginResponseDTO authenticateClient(LoginRequestDTO loginRequest, HttpServletResponse response);

    LoginResponseDTO registerClient(ClientDTO clientDTO, HttpServletResponse response);

    LoginResponseDTO selectEmployeeRoleInAuth(
            String token,
            Long roleId,
            HttpServletResponse response
    );

    void logout(HttpServletResponse response);
}
