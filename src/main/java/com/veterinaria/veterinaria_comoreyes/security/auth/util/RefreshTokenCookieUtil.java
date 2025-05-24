package com.veterinaria.veterinaria_comoreyes.security.auth.util;

import com.veterinaria.veterinaria_comoreyes.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenCookieUtil extends CookieUtil {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";

    public void setRefreshCookie(HttpServletResponse response, String token, int maxAge) {
        setCookie(response, REFRESH_COOKIE_NAME, token, maxAge, true, false, "Strict");
    }

    public void deleteRefreshCookie(HttpServletResponse response) {
        deleteCookie(response, REFRESH_COOKIE_NAME);
    }

    public Optional<String> getTokenFromCookies(HttpServletRequest request) {
        return getCookieValue(request, REFRESH_COOKIE_NAME);
    }
}
