package com.veterinaria.veterinaria_comoreyes.security.auth.util;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public String generateToken(Long userId, Long entityId, String roleName, List<String> permissions) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("entityId", entityId)
                .claim("roleName", roleName)
                .claim("perms", permissions)
                .claim("type", "access")
                .audience().add("your-audience").and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .require("aud", "your-audience") // ← Validación directa
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AuthException("Token expirado", ErrorCodes.TOKEN_EXPIRED.getCode());
        } catch (JwtException e) {
            throw new AuthException("Token inválido", ErrorCodes.INVALID_TOKEN.getCode());
        }
    }

    public Long getUserIdFromJwt(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    public Long getEntityIdFromJwt(String token) {
        Claims claims = parseToken(token);
        return claims.get("entityId", Long.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date getExpirationDateFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        }
    }
    public String refreshToken(String token) {
        Claims claims = extractAllClaims(token);
        Date now = new Date();
        Date newExpiration = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(claims.getSubject())
                .claim("type", "access")
                .issuedAt(now)
                .expiration(newExpiration)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }
}