package com.veterinaria.veterinaria_comoreyes.config;

import com.veterinaria.veterinaria_comoreyes.security.auth.JwtAuthorizationFilter;
import com.veterinaria.veterinaria_comoreyes.util.CookieUtil;
import com.veterinaria.veterinaria_comoreyes.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final CookieUtil cookieUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil,
                          AuthenticationConfiguration authenticationConfiguration,
                          CookieUtil cookieUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationConfiguration = authenticationConfiguration;
        this.cookieUtil = cookieUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 0) Habilitar CORS con tu fuente de configuración
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

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
                        new JwtAuthorizationFilter(jwtTokenUtil, cookieUtil),
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // o "*" en Postman
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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

