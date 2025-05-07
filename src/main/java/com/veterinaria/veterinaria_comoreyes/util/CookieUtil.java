package com.veterinaria.veterinaria_comoreyes.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CookieUtil {
    public void setJwtCookie(HttpServletResponse response, String token, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(false) // Cambiar a true en producción con HTTPS
                .path("/")
                .maxAge(maxAge)
                .sameSite("NONE")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public Optional<String> getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
}
