package com.veterinaria.veterinaria_comoreyes.security.auth.util;

import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
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
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public String generateToken(Long userId, Long entityId, String roleName, List<String> permissions) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("entityId", entityId)
                .claim("roleName", roleName)
                .claim("perms", permissions)
                .claim("type", "access")
                .setAudience("your-audience")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String refreshToken(String token) {
        Claims claims = extractAllClaims(token);
        Date now = new Date();
        Date newExpiration = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.getSubject())
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(newExpiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .requireAudience("your-audience")
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
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
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Date getExpirationDateFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration(); // Aún así retorna la fecha de expiración
        }
    }
}
