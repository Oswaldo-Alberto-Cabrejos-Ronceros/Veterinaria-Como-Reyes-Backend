package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAuthService;
import com.veterinaria.veterinaria_comoreyes.util.CookieUtil;
import com.veterinaria.veterinaria_comoreyes.util.JwtTokenUtil;
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
    private final CookieUtil cookieUtil;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(IAuthService authService,
                          CookieUtil cookieUtil,
                          JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login/employee")
    public ResponseEntity<LoginResponseDTO> loginEmployee(
            @RequestBody @Valid LoginRequestDTO request,
            HttpServletResponse response) {

        // 1) Llama al servicio que ya genera el token y el DTO
        LoginResponseDTO dto = authService.authenticateEmployee(request);

        // 2) Extrae el token y ponlo en cookie
        cookieUtil.setJwtCookie(
                response,
                dto.token(),
                jwtTokenUtil.getJwtExpirationMs() / 1000
        );

        // 3) Devuelve el DTO (sin el token en el body, o con él si lo quieres)
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login/client")
    public ResponseEntity<LoginResponseDTO> loginClient(
            @RequestBody @Valid LoginRequestDTO request,
            HttpServletResponse response) {

        LoginResponseDTO dto = authService.authenticateClient(request);

        cookieUtil.setJwtCookie(
                response,
                dto.token(),
                jwtTokenUtil.getJwtExpirationMs() / 1000
        );

        return ResponseEntity.ok(dto);
    }
}
