package com.veterinaria.veterinaria_comoreyes.security;

import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setJwtSecret("test-secret-key-12345678901234567890123456789012");
        jwtUtil.setJwtExpirationMs(3600000); // 1 hora
        jwtUtil.setRefreshExpirationMs(7200000); // 2 horas
        jwtUtil.init(); // importante para inicializar la clave secreta
    }

    @Test
    @DisplayName("Generar token JWT exitosamente")
    void testGenerateToken() {
        var auth = new UsernamePasswordAuthenticationToken(
                "test@correo.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
        );

        String token = jwtUtil.generateAccessToken(auth);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("Extraer correo desde token válido")
    void testGetEmailFromToken() {
        var auth = new UsernamePasswordAuthenticationToken("correo@ejemplo.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        String email = jwtUtil.getEmailFromJwt(token);
        assertEquals("correo@ejemplo.com", email);
    }

    @Test
    @DisplayName("Validar token correcto")
    void testValidateCorrectToken() {
        var auth = new UsernamePasswordAuthenticationToken("correo@ejemplo.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("Detectar token inválido")
    void testInvalidToken() {
        String invalidToken = "abc.def.ghi";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    @DisplayName("Detectar token expirado")
    void testExpiredToken() throws InterruptedException {
        jwtUtil.setJwtExpirationMs(1); // Expira en 1 ms
        jwtUtil.init();

        var auth = new UsernamePasswordAuthenticationToken("correo@expirado.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        Thread.sleep(5); // Esperar para que expire

        assertFalse(jwtUtil.validateToken(token));
        assertTrue(jwtUtil.isTokenExpired(token));
    }
}
