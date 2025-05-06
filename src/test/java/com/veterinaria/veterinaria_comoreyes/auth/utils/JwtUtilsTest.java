package com.veterinaria.veterinaria_comoreyes.auth.utils;

import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    private JwtUtil jwtUtil;
/*
    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setJwtSecret("clave-super-segura-123456789012345678901234"); // mínimo 32 bytes
        jwtUtil.setJwtExpirationMs(1000 * 60 * 60); // 1 hora
        jwtUtil.setRefreshExpirationMs(1000 * 60 * 60 * 24 * 5); // 5 días
        jwtUtil.init(); // prepara la clave firmadora
    }

    @Test
    @DisplayName("Generar y validar token correctamente")
    void testGenerateAndValidateToken() {
        var auth = new UsernamePasswordAuthenticationToken(
                "cliente@correo.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
        );

        String token = jwtUtil.generateAccessToken(auth);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token), "El token debería ser válido");
    }

    @Test
    @DisplayName("Extraer email desde token JWT")
    void testGetEmailFromToken() {
        var auth = new UsernamePasswordAuthenticationToken("prueba@email.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        String email = jwtUtil.getEmailFromJwt(token);
        assertEquals("prueba@email.com", email);
    }

    @Test
    @DisplayName("Detectar token expirado")
    void testExpiredToken() throws InterruptedException {
        jwtUtil.setJwtExpirationMs(1); // 1 milisegundo
        jwtUtil.init();

        var auth = new UsernamePasswordAuthenticationToken("expira@email.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        Thread.sleep(10); // esperar para asegurar expiración

        assertFalse(jwtUtil.validateToken(token), "Token debe estar expirado");
        assertTrue(jwtUtil.isTokenExpired(token));
    }

    @Test
    @DisplayName("Detectar token mal formado")
    void testInvalidToken() {
        String fakeToken = "abc.def.ghi";
        assertFalse(jwtUtil.validateToken(fakeToken), "Un token mal formado debe ser inválido");
    }*/
}
