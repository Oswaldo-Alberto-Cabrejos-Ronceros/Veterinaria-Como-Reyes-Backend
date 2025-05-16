package com.veterinaria.veterinaria_comoreyes.security.auth.filter;

import com.veterinaria.veterinaria_comoreyes.security.auth.models.JwtDetails;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.util.CookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtCookieUtil jwtCookieUtil;

    public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil, JwtCookieUtil jwtCookieUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtCookieUtil = jwtCookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // 1) Extraer token usando CookieUtil
        String token = jwtCookieUtil
                .getTokenFromCookies(request)
                .orElseGet(() -> {
                    String h = request.getHeader("Authorization");
                    return (h != null && h.startsWith("Bearer ")) ? h.substring(7) : null;
                });

        System.out.println(">> JWT token desde cookie/header: " + token);

        if (token != null && jwtTokenUtil.validateToken(token)) {
            Claims claims = jwtTokenUtil.parseToken(token);

            Long userId = Long.parseLong(claims.getSubject());
            Long entityId = claims.get("entityId", Long.class);
            String type = claims.get("type", String.class);

            @SuppressWarnings("unchecked")
            List<String> permissions = claims.get("perms", List.class);

            List<GrantedAuthority> authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            authentication.setDetails(new JwtDetails(entityId, type));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
