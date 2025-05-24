package com.veterinaria.veterinaria_comoreyes.security.auth.filter;

import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtRefreshTokenUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.RefreshTokenCookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAutoRefreshFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtCookieUtil jwtCookieUtil;
    @Autowired
    private JwtRefreshTokenUtil jwtRefreshTokenUtil;
    @Autowired
    private RefreshTokenCookieUtil refreshTokenCookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        Optional<String> jwtTokenOpt = jwtCookieUtil.getTokenFromCookies(request);
        Optional<String> refreshTokenOpt = refreshTokenCookieUtil.getTokenFromCookies(request);

        if (jwtTokenOpt.isPresent() && refreshTokenOpt.isPresent()) {
            String jwtToken = jwtTokenOpt.get();
            String refreshToken = refreshTokenOpt.get();

            // Verifica si el JWT está expirado o próximo a expirar (ej: en menos de 5 minutos)
            if (isTokenAboutToExpire(jwtToken)){
                if (jwtRefreshTokenUtil.validateRefreshToken(refreshToken)) {
                    // Extiende el token actual (mismos claims, nueva expiración)
                    String extendedToken = jwtTokenUtil.refreshToken(jwtToken);
                    jwtCookieUtil.setJwtCookie(response, extendedToken, jwtTokenUtil.getJwtExpirationMs() / 1000);
                }
            }
        }

        chain.doFilter(request, response);
    }

    // Método auxiliar para verificar si el token está próximo a expirar
    private boolean isTokenAboutToExpire(String token) {
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
        long timeLeft = expiration.getTime() - System.currentTimeMillis();
        return timeLeft < 5 * 60 * 1000; // 5 minutos antes de expirar
    }
}
