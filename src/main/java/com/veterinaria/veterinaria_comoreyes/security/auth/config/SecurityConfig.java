package com.veterinaria.veterinaria_comoreyes.security.auth.config;

import com.veterinaria.veterinaria_comoreyes.security.auth.filter.JwtAuthorizationFilter;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
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
                        JwtCookieUtil jwtCookieUtil,
                        IUserService userService) {
                this.jwtTokenUtil = jwtTokenUtil;
                this.authenticationConfiguration = authenticationConfiguration;
                this.jwtCookieUtil = jwtCookieUtil;
        }

        // @Bean
        // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //         http
        //                         .cors(cors -> cors.configurationSource(corsConfigurationSource))
        //                         .csrf(csrf -> csrf.disable())
        //                         .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //                         .authorizeHttpRequests(auth -> auth
        //                                         .requestMatchers(
        //                                                         "/api-docs/**",
        //                                                         "/swagger-ui-custom.html",
        //                                                         "/swagger-ui/**",
        //                                                         "/swagger-ui.html",
        //                                                         "/swagger-resources/**",
        //                                                         "/webjars/**",
        //                                                         "/v3/api-docs/**",
        //                                                         "/proxy/**",
        //                                                         "/actuator/**",
        //                                                         "/api/auth/**")
        //                                         .permitAll()
        //                                         .anyRequest().authenticated())
        //                         .addFilterBefore(
        //                                         new JwtAuthorizationFilter(jwtTokenUtil, jwtCookieUtil),
        //                                         UsernamePasswordAuthenticationFilter.class)
        //                         .exceptionHandling(ex -> ex
        //                                         .authenticationEntryPoint((req, res, ex2) -> res.sendError(
        //                                                         HttpServletResponse.SC_UNAUTHORIZED, "No autorizado"))
        //                                         .accessDeniedHandler((req, res, ex3) -> res.sendError(
        //                                                         HttpServletResponse.SC_FORBIDDEN, "Acceso denegado")));

        //         return http.build();
        // }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .csrf(csrf -> csrf.disable()) // Desactiva protección CSRF (para pruebas de API)
                                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().permitAll()
                                )
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint((req, res, ex2) -> res.sendError(
                                                                HttpServletResponse.SC_UNAUTHORIZED, "No autorizado"))
                                                .accessDeniedHandler((req, res, ex3) -> res.sendError(
                                                                HttpServletResponse.SC_FORBIDDEN, "Acceso denegado")));

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
                        throws Exception {
                return authConfig.getAuthenticationManager();
        }

}
