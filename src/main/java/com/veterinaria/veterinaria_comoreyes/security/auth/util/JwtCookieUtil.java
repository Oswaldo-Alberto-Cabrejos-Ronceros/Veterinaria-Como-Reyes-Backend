package com.veterinaria.veterinaria_comoreyes.security.auth.util;

import com.veterinaria.veterinaria_comoreyes.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtCookieUtil extends CookieUtil {
    private static final String JWT_COOKIE_NAME = "jwtToken";

    /**
     * Establece el token JWT en una cookie HTTP-only
     */
    public void setJwtCookie(HttpServletResponse response, String token, int maxAge) {
        // En producción cambiar secure a true cuando se use HTTPS
        setCookie(response, JWT_COOKIE_NAME, token, maxAge, true, false, "NONE");
    }

    /**
     * Elimina la cookie JWT
     */
    public void deleteJwtCookie(HttpServletResponse response) {
        deleteCookie(response, JWT_COOKIE_NAME);
    }

    /**
     * Extrae el token JWT de las cookies de la petición
     */
    public Optional<String> getTokenFromCookies(HttpServletRequest request) {
        return getCookieValue(request, JWT_COOKIE_NAME);
    }
}


