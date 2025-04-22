package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.service.impl.AuthenticationServiceImpl;
import com.veterinaria.veterinaria_comoreyes.service.impl.UserDetailsServiceImpl;
import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
    
    @Mock private JwtUtil jwtUtil;
    @Mock private IUserService userService;
    @Mock private IClientService clientService;
    @Mock private IEmployeeService employeeService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserDetailsServiceImpl userDetailsServiceImpl;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Autenticación exitosa de cliente")
    void testAuthenticateClientSuccess() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("client@mail.com", "1234");

        UserDetails userDetails = new User("client@mail.com", "1234", List.of());
        UserDTO userDTO = new UserDTO(); userDTO.setUserId(1L); userDTO.setEmail("client@mail.com");
        ClientDTO clientDTO = new ClientDTO(); clientDTO.setName("Juan");

        when(userDetailsServiceImpl.loadUserByUsername("client@mail.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("1234", "1234")).thenReturn(true);
        when(jwtUtil.generateAccessToken(any())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("refresh-token");
        when(userService.getUserByEmail("client@mail.com")).thenReturn(userDTO);
        when(clientService.getClientByUser(userDTO)).thenReturn(clientDTO);

        AuthenticationResponseDTO response = authenticationService.authenticateClient(loginRequestDTO);

        assertEquals("access-token", response.getJwtToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("Juan", response.getName());
    }

}
