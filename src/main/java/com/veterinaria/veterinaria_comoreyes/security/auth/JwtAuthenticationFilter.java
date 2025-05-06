package com.veterinaria.veterinaria_comoreyes.security.auth;

import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.models.CustomUserDetails;
import com.veterinaria.veterinaria_comoreyes.util.JwtTokenUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginRequestDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.email(),
                            credentials.password()
                    )
            );
        } catch (IOException e) {
            throw new AuthException("Error en autenticación", "AUTH-ERROR");
        }
    }

}
