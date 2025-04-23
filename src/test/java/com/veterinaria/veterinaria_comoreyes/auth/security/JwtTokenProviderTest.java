package com.veterinaria.veterinaria_comoreyes.auth.security;

import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setJwtSecret("clave-de-test-supersecreta-1234567890123456789012");
        jwtUtil.setJwtExpirationMs(3600000); // 1 hora
        jwtUtil.setRefreshExpirationMs(86400000); // 24 horas
        jwtUtil.init();
    }

    @Test
    @DisplayName("Genera token JWT correctamente")
    void testGenerateAccessToken() {
        var auth = new UsernamePasswordAuthenticationToken(
                "usuario@test.com", null, List.of(new SimpleGrantedAuthority("CLIENT"))
        );

        String token = jwtUtil.generateAccessToken(auth);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("Extrae email correctamente desde el token")
    void testGetEmailFromJwt() {
        var auth = new UsernamePasswordAuthenticationToken("email@test.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        String email = jwtUtil.getEmailFromJwt(token);

        assertEquals("email@test.com", email);
    }

    @Test
    @DisplayName("Valida token correcto")
    void testValidateCorrectToken() {
        var auth = new UsernamePasswordAuthenticationToken("valid@test.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Detecta token inválido")
    void testInvalidToken() {
        String fakeToken = "abc.def.ghi";
        assertFalse(jwtUtil.validateToken(fakeToken));
    }

    @Test
    @DisplayName("Detecta token expirado")
    void testExpiredToken() throws InterruptedException {
        jwtUtil.setJwtExpirationMs(1); // 1 ms
        jwtUtil.init();
        var auth = new UsernamePasswordAuthenticationToken("expira@test.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        Thread.sleep(5);

        assertTrue(jwtUtil.isTokenExpired(token));
        assertFalse(jwtUtil.validateToken(token));
    }
}
