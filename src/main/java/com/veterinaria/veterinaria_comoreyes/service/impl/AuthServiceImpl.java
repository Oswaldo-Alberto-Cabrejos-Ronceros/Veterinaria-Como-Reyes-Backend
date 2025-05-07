package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.auth.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.exception.ErrorCodes;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.repository.UserRepository;
import com.veterinaria.veterinaria_comoreyes.service.IAuthService;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmployeeServiceImpl employeeService;

    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            ClientRepository clientRepository,
            PasswordUtil passwordUtil,
            JwtTokenUtil jwtTokenUtil,
            EmployeeServiceImpl employeeService) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.employeeService = employeeService;
    }

    @Override
    public LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest) {
        // 1. Buscar usuario por email
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode()));

        // 2. Validar contraseña
        if (!passwordUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        // 3. Verificar si la cuenta está activa
        if (!user.getStatus()) {
            throw new AuthException("Cuenta desactivada", ErrorCodes.ACCOUNT_DISABLED.getCode());
        }

        // 4. Verificar tipo de usuario
        if (!"E".equals(user.getType())) {
            throw new AuthException("Acceso restringido a empleados", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        // 5. Obtener datos del empleado
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new AuthException("Empleado no encontrado", ErrorCodes.USER_NOT_FOUND.getCode()));

        // 6. Obtener permisos del empleado
        Set<String> permissions = new HashSet<>(employeeService.getEmployeePermissions(employee.getEmployeeId()));

        // 7. Generar token JWT
        String token = jwtTokenUtil.generateToken(
                user.getUserId(),
                employee.getEmployeeId(),
                "E",
                new ArrayList<>(permissions)
        );

        // 8. Retornar respuesta con token y datos
        return new LoginResponseDTO(
                token,
                user.getUserId(),
                employee.getEmployeeId(),
                "E",
                permissions
        );
    }

    @Override
    public LoginResponseDTO authenticateClient(LoginRequestDTO loginRequest) {
        // 1. Buscar usuario por email
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode()));

        // 2. Validar contraseña
        if (!passwordUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        // 3. Verificar si la cuenta está activa
        if (!user.getStatus()) {
            throw new AuthException("Cuenta desactivada", ErrorCodes.ACCOUNT_DISABLED.getCode());
        }

        // 4. Verificar tipo de usuario
        if (!"C".equals(user.getType())) {
            throw new AuthException("Acceso restringido a clientes", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        // 5. Obtener datos del cliente
        Client client = clientRepository.findByUser(user);

        // 6. Generar token JWT (clientes no tienen permisos)
        String token = jwtTokenUtil.generateToken(
                user.getUserId(),
                client.getClientId(),
                "C",
                Collections.emptyList()
        );

        // 7. Retornar respuesta con token y datos
        return new LoginResponseDTO(
                token,
                user.getUserId(),
                client.getClientId(),
                "C",
                Collections.emptySet()
        );
    }
}
