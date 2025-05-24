package com.veterinaria.veterinaria_comoreyes.security.auth.controller;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.models.CustomUserDetails;
import com.veterinaria.veterinaria_comoreyes.security.auth.service.IAuthService;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
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

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(
            @RequestBody @Valid ClientDTO clientDTO,
            HttpServletResponse response) {

        LoginResponseDTO dto = authService.registerClient(clientDTO, response);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/role/{roleId}")
    public ResponseEntity<LoginResponseDTO> selectEmployeeRole(
            @CookieValue("jwtToken") String token,
            @PathVariable Long roleId,
            HttpServletResponse response
    ) {
        LoginResponseDTO dto = authService.selectEmployeeRoleInAuth(
                token,
                roleId,
                response
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().body(Map.of("message", "Sesión cerrada correctamente"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshJwtToken(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(request, response);
        return ResponseEntity.ok("Token refrescado correctamente");
    }
}
