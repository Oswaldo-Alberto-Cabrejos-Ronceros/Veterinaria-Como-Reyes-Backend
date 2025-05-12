package com.veterinaria.veterinaria_comoreyes.security.auth.service;

import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.service.impl.EmployeeServiceImpl;
import com.veterinaria.veterinaria_comoreyes.util.PasswordEncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IUserService userService;
    private final IClientService clientService;
    private final PasswordEncodeUtil passwordEncodeUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmployeeServiceImpl employeeService;

    @Autowired
    public AuthServiceImpl(
            IUserService userService,
            IClientService clientService,
            PasswordEncodeUtil passwordEncodeUtil,
            JwtTokenUtil jwtTokenUtil,
            EmployeeServiceImpl employeeService) {
        this.userService = userService;
        this.clientService = clientService;
        this.passwordEncodeUtil = passwordEncodeUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.employeeService = employeeService;
    }

    @Override
    public LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest) {
        // 1. Buscar usuario por email
        User user = userService.getUserByEmailForAuth(loginRequest.email());

        // 2. Validar contraseña
        if (!passwordEncodeUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        // 3. Verificar si la cuenta está activa
        if (!user.getStatus()) {
            throw new AuthException("Cuenta Bloqueada.", ErrorCodes.ACCOUNT_DISABLED.getCode());
        }

        // 4. Verificar tipo de usuario
        if (!"E".equals(user.getType())) {
            throw new AuthException("Acceso restringido a empleados", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        // 5. Obtener datos del empleado
        Employee employee = employeeService.getEmployeeByUserForAuth(user);

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
        User user = userService.getUserByEmailForAuth(loginRequest.email());

        // 2. Validar contraseña
        if (!passwordEncodeUtil.matches(loginRequest.password(), user.getPassword())) {
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
        Client client = clientService.getClientByUserForAuth(user);

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
