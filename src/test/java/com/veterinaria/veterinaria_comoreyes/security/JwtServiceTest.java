package com.veterinaria.veterinaria_comoreyes.security;

import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Clase de prueba para validar el comportamiento de JwtUtil
public class JwtServiceTest {

    private JwtUtil jwtUtil;

    /**
     * Inicializa una instancia de JwtUtil antes de cada prueba.
     * Se configura con una clave secreta y tiempos de expiración.
     */
    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setJwtSecret("test-secret-key-12345678901234567890123456789012"); // clave segura
        jwtUtil.setJwtExpirationMs(3600000); // 1 hora
        jwtUtil.setRefreshExpirationMs(432000000); // 120 horas
        jwtUtil.init();
    }

    /**
     * Verifica que se genere correctamente un token JWT a partir de una autenticación válida.
     */
    @Test
    @DisplayName("Generar token JWT exitosamente")
    void testGenerateToken() {
        var auth = new UsernamePasswordAuthenticationToken(
                "test@correo.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
        );

        String token = jwtUtil.generateAccessToken(auth);

        assertNotNull(token, "El token no debe ser null");
        assertFalse(token.isBlank(), "El token no debe estar vacío");
    }

    /**
     * Extrae correctamente el email desde un token válido.
     */
    @Test
    @DisplayName("Extraer correo desde token válido")
    void testGetEmailFromToken() {
        var auth = new UsernamePasswordAuthenticationToken("correo@ejemplo.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        String email = jwtUtil.getEmailFromJwt(token);

        assertEquals("correo@ejemplo.com", email, "El email extraído debe coincidir con el original");
    }

    /**
     * Valida correctamente un token recién generado.
     */
    @Test
    @DisplayName("Validar token correcto")
    void testValidateCorrectToken() {
        var auth = new UsernamePasswordAuthenticationToken("correo@ejemplo.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        assertTrue(jwtUtil.validateToken(token), "El token debe ser válido");
    }

    /**
     * Detecta correctamente un token inválido con estructura incorrecta.
     */
    @Test
    @DisplayName("Detectar token inválido")
    void testInvalidToken() {
        String invalidToken = "abc.def.ghi";

        assertFalse(jwtUtil.validateToken(invalidToken), "El token inválido no debe pasar validación");
    }

    /**
     * Detecta que un token expirado ya no es válido.
     */
    @Test
    @DisplayName("Detectar token expirado")
    void testExpiredToken() throws InterruptedException {
        jwtUtil.setJwtExpirationMs(1); // expira en 1 ms
        jwtUtil.init();

        var auth = new UsernamePasswordAuthenticationToken("correo@expirado.com", null, List.of());
        String token = jwtUtil.generateAccessToken(auth);

        Thread.sleep(10); // esperar que expire

        assertFalse(jwtUtil.validateToken(token), "El token expirado no debe ser válido");
        assertTrue(jwtUtil.isTokenExpired(token), "Debe detectarse como expirado");
    }
}
