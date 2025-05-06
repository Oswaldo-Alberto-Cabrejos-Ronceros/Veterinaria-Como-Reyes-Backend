package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/employee")
    public ResponseEntity<LoginResponseDTO> loginEmployee(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.authenticateEmployee(request));
    }

    @PostMapping("/login/client")
    public ResponseEntity<LoginResponseDTO> loginClient(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.authenticateClient(request));
    }
}