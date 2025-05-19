package com.veterinaria.veterinaria_comoreyes.security.auth.controller;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.service.IAuthService;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtCookieUtil jwtCookieUtil;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(IAuthService authService,
                          JwtCookieUtil jwtCookieUtil,
                          JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtCookieUtil= jwtCookieUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login/employee")
    public ResponseEntity<LoginResponseDTO> loginEmployee(
            @RequestBody @Valid LoginRequestDTO request,
                                HttpServletResponse response) {
        // Llama al servicio, que ahora también recibe el response para setear la cookie
        LoginResponseDTO dto = authService.authenticateEmployee(request, response);

        // Retorna solo el DTO (sin el token, ya está en la cookie)
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/login/client")
    public ResponseEntity<LoginResponseDTO> loginClient(
            @RequestBody @Valid LoginRequestDTO request,
            HttpServletResponse response) {

        LoginResponseDTO dto = authService.authenticateClient(request, response);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login/register")
    public ResponseEntity<LoginResponseDTO> register(
            @RequestBody @Valid ClientDTO clientDTO,
            HttpServletResponse response) {

        LoginResponseDTO dto = authService.registerClient(clientDTO, response);

        return ResponseEntity.ok(dto);
    }
}
