package com.veterinaria.veterinaria_comoreyes.security.auth.service;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginRequestDTO;
import com.veterinaria.veterinaria_comoreyes.security.auth.dto.LoginResponseDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtCookieUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.service.impl.EmployeeServiceImpl;
import com.veterinaria.veterinaria_comoreyes.service.impl.PermissionServiceImpl;
import com.veterinaria.veterinaria_comoreyes.service.impl.RoleServiceImpl;
import com.veterinaria.veterinaria_comoreyes.util.PasswordEncodeUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
    private final JwtCookieUtil jwtCookieUtil;
    private final PermissionServiceImpl permissionService;
    private final RoleServiceImpl roleService;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(
            IUserService userService,
            IClientService clientService,
            PasswordEncodeUtil passwordEncodeUtil,
            JwtTokenUtil jwtTokenUtil,
            JwtCookieUtil jwtCookieUtil,
            EmployeeServiceImpl employeeService,
            PermissionServiceImpl permissionService,
            RoleServiceImpl roleService,
            UserMapper userMapper) {
        this.userService = userService;
        this.clientService = clientService;
        this.passwordEncodeUtil = passwordEncodeUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtCookieUtil = jwtCookieUtil;
        this.employeeService = employeeService;
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponseDTO authenticateEmployee(LoginRequestDTO loginRequest, HttpServletResponse response) {
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


        // 6. Decidir rol y permisos iniciales
        String nameRole;
        List<String> permissionsInToken;
        Map<String, List<String>> groupedPermissions = new HashMap<>();

        // 7. Obtener cantidad de roles asignados al empleado
        Long cantidadDeRoles =roleService.countActiveRolesForEmployee(employee.getEmployeeId());

        // 8. verificar si tiene 1 rol asignado
        if (cantidadDeRoles == 1) {
            List<Role> rolesActive = roleService.getActiveRolesForEmployee(employee.getEmployeeId());
            // Sólo un rol: lo asignamos directo
            Role role = rolesActive.get(0);
            nameRole= role.getName();

            // Usamos permissionService para traer actionCode y agrupados
            permissionsInToken = permissionService.permissionsByRoleId(role.getRoleId());
            groupedPermissions = permissionService.getGroupedPermissionsByRoleId(role.getRoleId());
        } else {
            // 0 ó ≥2 roles: token sin rol ni permisos, front deberá pedir selección
            nameRole = null;
            permissionsInToken = Collections.emptyList();
            groupedPermissions =null;
        }


        // 9. Generar token JWT
        String token = jwtTokenUtil.generateToken(
                user.getUserId(),
                employee.getEmployeeId(),
                nameRole,
                new ArrayList<>(permissionsInToken)
        );
        // Guardar en cookie
        jwtCookieUtil.setJwtCookie(response, token, jwtTokenUtil.getJwtExpirationMs() / 1000);


        // 8. Retornar respuesta con token y datos
        return new LoginResponseDTO(
                user.getUserId(),
                employee.getEmployeeId(),
                nameRole,
                groupedPermissions
        );
    }


    @Override
    public LoginResponseDTO authenticateClient(LoginRequestDTO loginRequest, HttpServletResponse response) {
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
        // Guardar en cookie
        jwtCookieUtil.setJwtCookie(response, token, jwtTokenUtil.getJwtExpirationMs() / 1000);


        // 7. Retornar respuesta con token y datos
        return new LoginResponseDTO(

                user.getUserId(),
                client.getClientId(),
                "Cliente",
                null
        );
    }

    @Override
    public LoginResponseDTO registerClient(ClientDTO clientDTO, HttpServletResponse response) {

        ClientDTO client = clientService.createClient(clientDTO);

        // 6. Generar token JWT (clientes no tienen permisos)
        String token = jwtTokenUtil.generateToken(
                client.getUser().getUserId(),
                client.getClientId(),
                "C",
                Collections.emptyList()
        );
        // Guardar en cookie
        jwtCookieUtil.setJwtCookie(response, token, jwtTokenUtil.getJwtExpirationMs() / 1000);


        // 7. Retornar respuesta con token y datos
        return new LoginResponseDTO(

                client.getUser().getUserId(),
                client.getClientId(),
                "Cliente",
                null
        );

    }

    @Transactional
    @Override
    public LoginResponseDTO selectEmployeeRoleInAuth(
            String token,
            Long roleId,
            HttpServletResponse response
    ) {
        Long userId = jwtTokenUtil.getUserIdFromJwt(token);

        // 1. Obtener al user y al employee
        UserDTO userDTO = userService.getUserById(userId);
        User user = userMapper.maptoUser(userDTO);

        Employee emp = employeeService.getEmployeeByUserForAuth(user);

        // 2. Obtener permisos (action_code) de ese rol par el spring
        List<String> permisos = permissionService.permissionsByRoleId(roleId);

        //3. obtener el nombre del Rol escogido
        String nameRole = emp.getRoles().stream()
                .filter(r -> r.getRoleId().equals(roleId))
                .findFirst()
                .orElseThrow(() -> new AuthException(
                        "Rol no asignado al empleado",
                        ErrorCodes.INVALID_ROLE.getCode()
                ))
                .getName();

        // 4. Generar nuevo token con el rol escogido y sus permisos correspondientes
        String newToken = jwtTokenUtil.generateToken(
                user.getUserId(),
                emp.getEmployeeId(),
                nameRole,
                permisos
        );

        // 5. Reemplazar cookie
        jwtCookieUtil.setJwtCookie(response, newToken, jwtTokenUtil.getJwtExpirationMs() / 1000);

        // 6. Agrupar permisos para el front
        Map<String, List<String>> permissionsInGruop = permissionService.getGroupedPermissionsByRoleId(roleId);

        // 7. Devolver DTO con el nuevo rol y permisos
        return new LoginResponseDTO(
                user.getUserId(),
                emp.getEmployeeId(),
                nameRole,
                permissionsInGruop
        );
    }

    @Override
    public void logout(HttpServletResponse response) {
        // Aquí eliminamos la cookie JWT
        jwtCookieUtil.deleteJwtCookie(response);
        // Si tienes cache o tokens en Redis, aquí también limpiarías (si aplica)
    }

}
