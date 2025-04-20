package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.models.AuthenticationResponse;
import com.veterinaria.veterinaria_comoreyes.models.LoginRequest;

public interface IAuthenticationService {
    //for authenticate client
    public AuthenticationResponse authenticateClient(LoginRequest loginRequest);
    public AuthenticationResponse authenticateEmployee(LoginRequest loginRequest);
    //para refrescar token
    public String refreshToken(String refreshToken);
    //solo para registrar clientes
    public AuthenticationResponse registerUser(User user);
}
