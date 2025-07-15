package com.veterinaria.veterinaria_comoreyes.external.reports.utils;

import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final JwtCookieUtil jwtCookieUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final HttpServletRequest request;
    private final EmployeeRepository employeeRepository;

    public Employee getAuthenticatedEmployee() {
        log.info("[SecurityUtil] Iniciando extracción de empleado autenticado...");

        String token = jwtCookieUtil.getTokenFromCookies(request)
                .orElseThrow(() -> new RuntimeException("Token no encontrado"));

        log.info("[SecurityUtil] Token JWT extraído: {}", token);

        Long employeeId = jwtTokenUtil.getEntityIdFromJwt(token); // Este es el employeeId
        log.info("[SecurityUtil] ID del empleado extraído del token: {}", employeeId);

        return employeeRepository.findByEmployeeIdAndStatusTrue(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }
}
