package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.service.IAuthenticationService;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import com.veterinaria.veterinaria_comoreyes.util.PasswordUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    /*
    private final JwtUtil jwtUtil;
    private final IUserService userService;
    private final IClientService clientService;
    private final IEmployeeService employeeService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordUtil passwordUtil;

    public AuthenticationServiceImpl(
            JwtUtil jwtUtil,
            IUserService userService,
            IClientService clientService,
            IEmployeeService employeeService,
            UserDetailsServiceImpl userDetailsServiceImpl,
            PasswordUtil passwordUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordUtil=passwordUtil;
    }

    @Override
    public AuthenticationResponseDTO authenticateClient(LoginRequestDTO loginRequestDTO) {
        return null;
    }

    @Override
    public AuthenticationResponseDTO authenticateEmployee(LoginRequestDTO loginRequestDTO) {
        return null;
    }

    @Override
    public String refreshToken(String refreshToken) {
        return "";
    }

    @Transactional
@Override
public AuthenticationResponseDTO registerUserClient(RegisterRequestDTO newClient) {
    // Validar teléfono
    phoneUtil.validatePhoneAvailable(newClient.getPhone(), "cliente");

    // Validar que no exista cliente con mismo DNI
    if (clientRepository.existsByDni(newClient.getDni())) {
        throw new RuntimeException("Cliente ya registrado con ese DNI: " + newClient.getDni());
    }

    // Validar con Reniec
    reniecUtil.validateData(
            newClient.getDni(),
            newClient.getName(),
            newClient.getLastName()
    );

    // Validar sede
    headquarterUtil.validateHeadquarterAvailable(newClient.getIdHeadquarter());

    // Crear el usuario
    UserDTO userDTO = new UserDTO();
    userDTO.setType("C");
    userDTO.setEmail(newClient.getEmail());
    userDTO.setPassword(newClient.getPassword());

    UserDTO savedUserDTO = userService.createUser(userDTO);
    User savedUser = UserMapper.maptoUser(savedUserDTO);

    // Construir ClientDTO
    ClientDTO clientDTO = new ClientDTO();
    clientDTO.setDni(newClient.getDni());
    clientDTO.setName(newClient.getName());
    clientDTO.setLastName(newClient.getLastName());
    clientDTO.setAddress(newClient.getAddress());
    clientDTO.setPhone(newClient.getPhone());
    clientDTO.setBirthDate(newClient.getBirthDate());
    clientDTO.setDirImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgpnmY-O9iz09Jka-vGvK2Lv-U-pL3H18CfA&s");

    // Obtener sede activa
    Headquarter headquarter = headquarterRepository.findById(newClient.getIdHeadquarter())
            .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + newClient.getIdHeadquarter()));
    clientDTO.setHeadquarter(headquarter);

    // Asociar el User al DTO
    clientDTO.setUser(savedUser);

    // Mapear a entidad y guardar
    Client clientEntity = ClientMapper.mapToClient(clientDTO);
    clientEntity.setStatus(true);

    clientRepository.save(clientEntity);

    // Retornar respuesta (luego aquí agregas token)
    return new AuthenticationResponseDTO();
}

    /*
    @Override
    public AuthenticationResponseDTO authenticateClient(LoginRequestDTO loginRequestDTO) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequestDTO.getEmail());
        System.out.println(userDetails.getPassword());
        System.out.println(loginRequestDTO.getPassword());
        if (passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            // generamos token
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,
                    userDetails.getAuthorities());
            String jwtToken = jwtUtil.generateAccessToken(authentication);
            // generamos refresh token
            String refreshToken = jwtUtil.generateRefreshToken(authentication);
            // obtenemos al client
            UserDTO userDTO = userService.getUserByEmail(userDetails.getUsername());
            ClientDTO clientDTO = clientService.getClientByUser(userDTO);
            return ClientMapper.mapToAuthenticationResponseDTO(userDTO.getUserId(), clientDTO, jwtToken, refreshToken);
        } else {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

    @Override
    public AuthenticationResponseDTO authenticateEmployee(LoginRequestDTO loginRequestDTO) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(loginRequestDTO.getEmail());
        if (passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword())) {
            // generamos token
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,
                    userDetails.getAuthorities());
            String jwtToken = jwtUtil.generateAccessToken(authentication);
            // generamos refresh token
            String refreshToken = jwtUtil.generateRefreshToken(authentication);
            // obtenemos al client
            UserDTO userDTO = userService.getUserByEmail(userDetails.getUsername());
            EmployeeDTO employeeDTO = employeeService.getEmployeeByUser(userDTO);
            return EmployeeMapper.mapToAuthenticationResponseDTO(userDTO.getUserId(), employeeDTO, jwtToken,
                    refreshToken);
        } else {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        boolean isValid = jwtUtil.validateToken(refreshToken);
        if (isValid) {
            String email = jwtUtil.getEmailFromJwt(refreshToken);
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,
                    userDetails.getAuthorities());
            return jwtUtil.generateAccessToken(authentication);
        } else {
            throw new JwtException("Error al validad token de refresco");
        }
    }

    @Override
    public AuthenticationResponseDTO registerUserClient(ClientDTO clientDTO, UserDTO userDTO) {
        userDTO.setType("C");
        UserDTO userSaved = userService.createUser(userDTO);
        clientDTO.setUser(UserMapper.maptoUser(userSaved));
        ClientDTO clientSaved = clientService.createClient(clientDTO);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userDTO.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,
                userDetails.getAuthorities());

        String jwtToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(authentication);


        return ClientMapper.mapToAuthenticationResponseDTO(userSaved.getUserId(),clientSaved,jwtToken,refreshToken);
    }*/


    

}
