package com.veterinaria.veterinaria_comoreyes.external.reports.utils;

import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final JwtCookieUtil jwtCookieUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final HttpServletRequest request;
    private final EmployeeRepository employeeRepository;

    public Employee getAuthenticatedEmployee() {
        String token = jwtCookieUtil.getTokenFromCookies(request)
                .orElseThrow(() -> new RuntimeException("Token no encontrado"));
        Long idEmpleado = jwtTokenUtil.getEntityIdFromJwt(token);
        return employeeRepository.findByUser_UserIdAndStatusTrue(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }
}
