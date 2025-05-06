package com.veterinaria.veterinaria_comoreyes.config;

import com.veterinaria.veterinaria_comoreyes.security.auth.JwtAuthenticationFilter;
import com.veterinaria.veterinaria_comoreyes.security.auth.JwtAuthorizationFilter;
import com.veterinaria.veterinaria_comoreyes.service.impl.UserDetailsServiceImpl;
import com.veterinaria.veterinaria_comoreyes.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    // Constructor para inyectar las dependencias
    public SecurityConfig(JwtTokenUtil jwtTokenUtil, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración básica
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                );

        // Configuración de filtros
        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);
        http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtTokenUtil))
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);

        // Manejo de errores
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                })
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}





    /*
    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationProvider authenticationProvider) throws Exception {
        http.authenticationProvider(authenticationProvider);
        return http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        httpRequest -> {
                            httpRequest.requestMatchers("/auth/**").permitAll();
                            //permision
                            httpRequest.requestMatchers(                "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**").permitAll();
                            httpRequest.requestMatchers("/api/**").permitAll();
                            httpRequest.anyRequest().authenticated();
                        }
                ).exceptionHandling(
                        exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"UNAUTHORIZED\"}");
                        }).accessDeniedHandler((equest, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"error\": \"UNAUTHORIZED\"}");
                        })
                ).addFilterBefore(new JwtAuthFilter(jwtUtil), BasicAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider( UserDetailsServiceImpl userDetailsServiceImp) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImp::loadUserByUsername);
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration= new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173","https://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

