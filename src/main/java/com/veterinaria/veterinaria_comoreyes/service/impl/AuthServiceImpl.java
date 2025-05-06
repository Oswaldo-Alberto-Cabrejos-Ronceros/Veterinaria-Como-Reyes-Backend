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
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordUtil passwordUtil;
    private final IEmployeeService employeeService;

    public AuthServiceImpl(UserRepository userRepository, EmployeeRepository employeeRepository,
                           ClientRepository clientRepository, JwtTokenUtil jwtTokenUtil,
                           PasswordUtil passwordUtil,IEmployeeService employeeService) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordUtil = passwordUtil;
        this.employeeService = employeeService;
    }

    @Override
    public LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode()));

        if (!passwordUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        if (user.getStatus().equals(Boolean.FALSE)) {
            throw new AuthException("Cuenta Bloqueada", ErrorCodes.ACCOUNT_DISABLED.getCode());
        }

        if (!user.getType().equals("E")) {
            throw new AuthException("Acceso restringido a empleados", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new AuthException("Empleado no encontrado", ErrorCodes.USER_NOT_FOUND.getCode()));


        Set<String> permissions = new HashSet<>(employeeService.getEmployeePermissions(employee.getEmployeeId()));

        String token = jwtTokenUtil.generateToken(user.getUserId(), employee.getEmployeeId(), "E", new ArrayList<>(permissions));

        return new LoginResponseDTO(token, user.getUserId(), employee.getEmployeeId(), "E", permissions);
    }

    @Override
    public LoginResponseDTO authenticateClient(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode()));

        if (!passwordUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthException("Credenciales inválidas", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        if (user.getStatus().equals(Boolean.FALSE)) {
            throw new AuthException("Cuenta Bloqueada", ErrorCodes.ACCOUNT_DISABLED.getCode());
        }

        if (!user.getType().equals("C")) {
            throw new AuthException("Acceso restringido a clientes", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }

        Client client = clientRepository.findByUser(user);

        String token = jwtTokenUtil.generateToken(user.getUserId(), client.getClientId(), "C", List.of());

        return new LoginResponseDTO(token, user.getUserId(), client.getClientId(), "C", Collections.emptySet());
    }
}
