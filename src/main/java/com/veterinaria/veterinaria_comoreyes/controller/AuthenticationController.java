package com.veterinaria.veterinaria_comoreyes.controller;


import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.service.IAuthenticationService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthenticationController {

    /*
    private final IAuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //funcion para agregar cookie a la respuesta
    private void addCokkie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name,value);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setSecure(false); //en produccion ira en true al trabajar en https
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/login/client")
    public ResponseEntity<AuthenticationResponseDTO> loginClient(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticateClient(loginRequestDTO);
        //configuramos cookies httponly
        addCokkie(response,"jwtToken",authenticationResponseDTO.getJwtToken());
        addCokkie(response,"refreshToken",authenticationResponseDTO.getRefreshToken());
        AuthenticationResponseDTO responseToSend = new AuthenticationResponseDTO(authenticationResponseDTO.getUsuarioId(), authenticationResponseDTO.getName(), authenticationResponseDTO.getRole());
        return ResponseEntity.ok(responseToSend);
    }

    @PostMapping("/login/employee")
    public ResponseEntity<AuthenticationResponseDTO> loginEmployee(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticateEmployee(loginRequestDTO);
        //configuramos cookies httponly
        addCokkie(response,"jwtToken",authenticationResponseDTO.getJwtToken());
        addCokkie(response,"refreshToken",authenticationResponseDTO.getRefreshToken());
        AuthenticationResponseDTO responseToSend = new AuthenticationResponseDTO(authenticationResponseDTO.getUsuarioId(), authenticationResponseDTO.getName(), authenticationResponseDTO.getRole());
        return ResponseEntity.ok(responseToSend);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Cookie> refreshToken = Arrays.stream(cookies).filter(c -> c.getName().equals("refreshToken")).findFirst();
        if (refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String newAccessToken = authenticationService.refreshToken(refreshToken.get().getValue());
            //creamos nueva cookie
            addCokkie(response,"jwtToken",newAccessToken);
            return ResponseEntity.ok(Map.of("Message", "Token refrescado correctamente"));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO, HttpServletResponse response) {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.registerUserClient(registerRequestDTO.getClient(),registerRequestDTO.getUser());
        //configuramos cookies httponly
        addCokkie(response,"jwtToken",authenticationResponseDTO.getJwtToken());
        addCokkie(response,"refreshToken",authenticationResponseDTO.getRefreshToken());
        AuthenticationResponseDTO responseToSend = new AuthenticationResponseDTO(authenticationResponseDTO.getUsuarioId(), authenticationResponseDTO.getName(), authenticationResponseDTO.getRole());
        return ResponseEntity.ok(responseToSend);
    }
*/
}
