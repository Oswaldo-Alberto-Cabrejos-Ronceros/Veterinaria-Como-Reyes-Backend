package com.veterinaria.veterinaria_comoreyes.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CookieUtil {

    /**
     * Crea una cookie con los parámetros especificados
     */
    protected ResponseCookie createCookie(String name, String value, int maxAge,
                                          boolean httpOnly, boolean secure, String sameSite) {
        return ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .secure(secure)
                .path("/")
                .maxAge(maxAge)
                .sameSite(sameSite)
                .build();
    }

    /**
     * Establece una cookie en la respuesta HTTP
     */
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge,
                          boolean httpOnly, boolean secure, String sameSite) {
        ResponseCookie cookie = createCookie(name, value, maxAge, httpOnly, secure, sameSite);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Elimina una cookie estableciendo su tiempo de vida a 0
     */
    public void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = createCookie(name, "", 0, true, false, "Lax");
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Obtiene el valor de una cookie por su nombre
     */
    public Optional<String> getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
}