package com.veterinaria.veterinaria_comoreyes.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    //getting the secret key
    @Value("${jwt.secret}")
    private String secretKey;
    //inicialize key
    private Key key;
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    //for access token
    public String generateAccessToken(Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).claim("authorities", authorities).setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)).signWith(key).compact();
    }

    //for refresh token
    public String generateRefreshToken(Authentication authentication) {
        String email = authentication.getPrincipal().toString();
        return Jwts.builder().setSubject(email).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 120 * 60 * 60 * 1000)).signWith(key).compact();
    }


    //for validate token
    public boolean validateToken(String token) {
        try {
            //para verificar, si falla el token es invalido
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            //para verificar si el token expiro
            boolean isExpired = claims.getExpiration().before(new Date());
            String emailToken = claims.getSubject();

            //no tiene que estar espirado y el email debe ser valido
            return !isExpired && emailToken != null && !emailToken.trim().isEmpty();
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //for getting email from token
    public String getEmailFromJwt(String token) {
        return getClaimsIfValid(token).getSubject();
    }

    //for getting authorities from token
    public List<GrantedAuthority> getAuthoritiesFromJwt(String token) {
        String authorities = getClaimsIfValid(token).get("authorities", String.class);
        return List.of(authorities.split(",")).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //for verifier if token is expired
    public boolean isTokenExpired(String token) {
        try {
            //obtenemos la fecha de expiracion del token
            Date expirateDate = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
            return expirateDate.before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    //for getting claims
    private Claims getClaimsIfValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            if (claims.getExpiration().before(new Date())) {
                throw new JwtException("Token expired");
            }
            return claims;
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }


}
