package com.veterinaria.veterinaria_comoreyes.security.auth.config;

import com.veterinaria.veterinaria_comoreyes.security.auth.filter.JwtAuthorizationFilter;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtCookieUtil jwtCookieUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil,
                          AuthenticationConfiguration authenticationConfiguration,
                          JwtCookieUtil jwtCookieUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtCookieUtil = jwtCookieUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 0) Habilitar CORS con tu fuente de configuración
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 1) No necesitamos csrf porque usamos JWT en cookie
                .csrf(csrf -> csrf.disable())

                // 2) Stateless: no mantenemos sesión HTTP
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3) Rutas públicas vs protegidas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // login, refresh, logout...
                        .anyRequest().authenticated()                 // el resto requiere JWT válido
                )

                // 4) Filtro de autorización: lee cookie “jwtToken” y valida
                .addFilterBefore(
                        new JwtAuthorizationFilter(jwtTokenUtil, jwtCookieUtil),
                        UsernamePasswordAuthenticationFilter.class
                )

                // 5) Manejo de errores de seguridad
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, ex2) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado"))
                        .accessDeniedHandler((req, res, ex3) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado"))
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }


    }

