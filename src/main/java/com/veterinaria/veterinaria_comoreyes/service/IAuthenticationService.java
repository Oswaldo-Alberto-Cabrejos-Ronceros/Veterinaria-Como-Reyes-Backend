package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.dto.AuthenticationResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.LoginRequestDTO;

public interface IAuthenticationService {
    //for authenticate client
    AuthenticationResponseDTO authenticateClient(LoginRequestDTO loginRequestDTO);
    AuthenticationResponseDTO authenticateEmployee(LoginRequestDTO loginRequestDTO);
    //para refrescar token
    String refreshToken(String refreshToken);
    //solo para registrar clientes
    AuthenticationResponseDTO registerUserClient(ClientDTO clientDTO, UserDTO userDTO);
}
