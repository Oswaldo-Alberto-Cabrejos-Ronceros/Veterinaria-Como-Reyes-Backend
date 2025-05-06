package com.veterinaria.veterinaria_comoreyes.util;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void setJwtCookie(HttpServletResponse response, String token, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(true) // Solo en producción con HTTPS
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
