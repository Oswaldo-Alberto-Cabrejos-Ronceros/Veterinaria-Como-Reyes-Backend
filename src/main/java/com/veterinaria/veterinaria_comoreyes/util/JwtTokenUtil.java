package com.veterinaria.veterinaria_comoreyes.util;

import com.veterinaria.veterinaria_comoreyes.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.exception.ErrorCodes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;


@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        // Usar Keys.secretKeyFor para generar una clave segura para HS512
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    // Método para obtener el tiempo de expiración
    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public String generateToken(Long userId, Long entityId, String type, List<String> permissions) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("entityId", entityId)
                .claim("type", type)
                .claim("perms", permissions)
                .setAudience("your-audience") // Add this line
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key) // Usar la clave para verificar la firma
                    .build()
                    .parseClaimsJws(token) // Parsear el token
                    .getBody(); // Obtener las reclamaciones
        } catch (ExpiredJwtException e) {
            throw new AuthException("Token expirado", ErrorCodes.TOKEN_EXPIRED.getCode());
        } catch (JwtException e) {
            throw new AuthException("Token inválido", ErrorCodes.INVALID_TOKEN.getCode());
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .requireAudience("your-audience") // <-- THIS LINE HERE
                    .setSigningKey(key) // Verificar la firma
                    .build()
                    .parseClaimsJws(token); // Intentar parsear el token
            return true; // Si no lanza excepciones, el token es válido
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Si hay una excepción, el token no es válido
        }
    }

    // EXTRAER EL ID DEL TOKEN PARA CONFIRMAR IDENTIDAD IN ACTIONS
    public Long getEntityIdFromJwt(String token) {
        Claims claims = parseToken(token);
        return claims.get("entityId", Long.class);
    }
}