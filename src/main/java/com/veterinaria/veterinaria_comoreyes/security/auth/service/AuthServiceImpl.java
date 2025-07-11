package com.veterinaria.veterinaria_comoreyes.security.auth.service;

import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
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
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtRefreshTokenUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.RefreshTokenCookieUtil;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.service.impl.EmployeeServiceImpl;
import com.veterinaria.veterinaria_comoreyes.service.impl.PermissionServiceImpl;
import com.veterinaria.veterinaria_comoreyes.service.impl.RoleServiceImpl;
import com.veterinaria.veterinaria_comoreyes.util.PasswordEncodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements IAuthService {
    /**
     * Definir si el refrestToken para ambos o solo para empleados
     */

    private final IUserService userService;
    private final IClientService clientService;
    private final PasswordEncodeUtil passwordEncodeUtil;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmployeeServiceImpl employeeService;
    private final JwtCookieUtil jwtCookieUtil;
    private final PermissionServiceImpl permissionService;
    private final RoleServiceImpl roleService;
    private final UserMapper userMapper;
    private final JwtRefreshTokenUtil jwtRefreshTokenUtil;
    private final RefreshTokenCookieUtil refreshTokenCookieUtil;

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
            UserMapper userMapper,
            JwtRefreshTokenUtil jwtRefreshTokenUtil,
            RefreshTokenCookieUtil refreshTokenCookieUtil
            ) {
        this.userService = userService;
        this.clientService = clientService;
        this.passwordEncodeUtil = passwordEncodeUtil;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtCookieUtil = jwtCookieUtil;
        this.employeeService = employeeService;
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.jwtRefreshTokenUtil = jwtRefreshTokenUtil;
        this.refreshTokenCookieUtil = refreshTokenCookieUtil;
    }

    /**
     * Autentificacion si es empleado
     */
    @Override
    public LoginResponseDTO authenticateEmployee(
            LoginRequestDTO loginRequest,
            HttpServletResponse response
    ) {
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
            // 0 ó ≥2 roles: token con el rol de mayor jerarquia

            nameRole = employeeService.getMainRoleName(employee.getEmployeeId());
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

        // Generar ek refreshToken
        String refreshToken =jwtRefreshTokenUtil.generateRefreshToken(user.getUserId());

        //Guardar en cookie el refresh
        refreshTokenCookieUtil.setRefreshCookie(response, refreshToken, jwtRefreshTokenUtil.getRefreshExpirationMs() / 1000);

        // 8. Retornar respuesta con token y datos
        return new LoginResponseDTO(
                user.getUserId(),
                employee.getEmployeeId(),
                nameRole,
                groupedPermissions
        );
    }

    /**
     * Autentificacion si es cliente
     */
    @Override
    public LoginResponseDTO authenticateClient(
            LoginRequestDTO loginRequest,
            HttpServletResponse response
    ) {
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

    /**
     * Registrarse como Cliente
     */
    @Override
    public LoginResponseDTO registerClient(
            ClientDTO clientDTO,
            HttpServletResponse response
    ) {

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

    /**
     * Asignar rol al sistema en auth en los empleados que tengan 2 o mas roles
     */
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

    /**
     * Cerrar Sesion
     */
    @Override
    public void logout(
            HttpServletResponse response
    ) {
        // Elimina la cookie JWT
        jwtCookieUtil.deleteJwtCookie(response);

        // Elimina la cookie Refresh Token
        refreshTokenCookieUtil.deleteRefreshCookie(response);
    }

    /**
     * Refresca el JWT extendiendo su tiempo de expiración si el refreshToken es válido. (userId de refreshToken es igual al del Token nomral)
     */
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> optionalRefreshToken = refreshTokenCookieUtil.getTokenFromCookies(request);
        Optional<String> optionalJwtToken = jwtCookieUtil.getTokenFromCookies(request);

        if (optionalRefreshToken.isEmpty() || optionalJwtToken.isEmpty()) {
            throw new RuntimeException("Tokens faltantes en las cookies");
        }

        String refreshToken = optionalRefreshToken.get();
        String jwtToken = optionalJwtToken.get();

        if (!jwtRefreshTokenUtil.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        if (!jwtTokenUtil.validateToken(jwtToken)) {
            throw new RuntimeException("JWT inválido o expirado");
        }

        Long userIdFromRefresh = jwtRefreshTokenUtil.getUserIdFromToken(refreshToken);
        Long userIdFromJwt = jwtTokenUtil.getUserIdFromJwt(jwtToken);

        if (!userIdFromRefresh.equals(userIdFromJwt)) {
            throw new RuntimeException("Los tokens no pertenecen al mismo usuario");
        }

        // Reutiliza el token, extendiendo su tiempo
        String refreshedToken = jwtTokenUtil.refreshToken(jwtToken);

        // Guardar el nuevo token en cookie
        jwtCookieUtil.setJwtCookie(response, refreshedToken, jwtTokenUtil.getJwtExpirationMs() / 1000);
    }


}
