package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
import com.veterinaria.veterinaria_comoreyes.exception.PhoneAlreadyExistsException;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.external.reniec.service.IReniecService;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final IUserService userService;
    private final IHeadquarterService headquarterService;
    private final IReniecService reniecService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;
    private final HeadquarterMapper headquarterMapper;

    @Autowired
    public ClientServiceImpl(
        ClientRepository clientRepository,
        IUserService userService,
        IReniecService reniecService,
        IHeadquarterService headquarterService,
        JwtTokenUtil jwtTokenUtil,
        HeadquarterMapper headquarterMapper,
        ClientMapper clientMapper,
        UserMapper userMapper
    ) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.headquarterService = headquarterService;
        this.reniecService = reniecService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientMapper = clientMapper;
        this.headquarterMapper = headquarterMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Page<ClientListDTO> searchClients(String dni, String name, String lastName, Boolean status, Long headquarterId, Pageable pageable) {
        return clientRepository.searchClients(dni, name, lastName, status, headquarterId, pageable);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return clientMapper.mapToClientDTO(client);
    }

    @Override
    public ClientDTO getClientByUser(UserDTO userDTO) {
        Client client = clientRepository.findByUser(userMapper.maptoUser(userDTO));

        return clientMapper.mapToClientDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
            .stream()
            .map(clientMapper::mapToClientDTO)
            .collect(Collectors.toList());
    }

    private void validatePhoneAvailable(String phone){
        boolean exist = clientRepository.existsByPhone(phone);
        if (exist) {
            throw new PhoneAlreadyExistsException("El número de teléfono ya está registrado en otro empleado");
        }
    }

    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {


        validatePhoneAvailable(clientDTO.getPhone());

        if (clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Cliente ya registrado con ese DNI: " + clientDTO.getDni());
        }

        reniecService.validateIdentityReniec(clientDTO.getDni(), clientDTO.getName(), clientDTO.getLastName());

        headquarterService.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());

        System.out.println("Client ID antes de guardar: " + clientDTO.getClientId());
        if (clientDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("C");
            userDTO.setEmail(clientDTO.getUser().getEmail());
            userDTO.setPassword(clientDTO.getUser().getPassword());

            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = userMapper.maptoUser(savedUserDTO);
            clientDTO.setUser(savedUser);
        }

        System.out.println("User ID: " + clientDTO.getUser().getUserId());


        Client client = clientMapper.mapToClient(clientDTO);
        client.setStatus(true);
        return clientMapper.mapToClientDTO(clientRepository.save(client));
    }

    @Transactional
    @Override
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findByClientId(clientId);

        validatePhoneAvailable(clientDTO.getPhone());

        if (!existingClient.getDni().equals(clientDTO.getDni()) &&
                clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Otro cliente ya registrado con el mismo DNI: " + clientDTO.getDni());
        } else {
            reniecService.validateIdentityReniec(clientDTO.getDni(), clientDTO.getName(), clientDTO.getLastName());
        }

        headquarterService.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());
        existingClient.getHeadquarter().setHeadquarterId(clientDTO.getHeadquarter().getHeadquarterId());

        existingClient.setDni(clientDTO.getDni());
        existingClient.setName(clientDTO.getName());
        existingClient.setLastName(clientDTO.getLastName());
        existingClient.setAddress(clientDTO.getAddress());
        existingClient.setPhone(clientDTO.getPhone());
        existingClient.setBirthDate(clientDTO.getBirthDate());
        existingClient.setDirImage(clientDTO.getDirImage());

        return clientMapper.mapToClientDTO(clientRepository.save(existingClient));
    }

    @Transactional
    @Override
    public void blockClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        client.setStatus(false);
        clientRepository.save(client);
    }

    @Transactional
    @Override
    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        client.setStatus(false); // eliminación lógica
        clientRepository.save(client);
    }

    @Override
    public MyInfoClientDTO myInfoAsClient(String token, Long id) {
        Long clientIdFromToken = Long.valueOf(jwtTokenUtil.getEntityIdFromJwt(token));
        if (!clientIdFromToken.equals(id)) {
            throw new RuntimeException("No tienes permiso para acceder a esta información.");
        }

        Client client = clientRepository.findByClientId(id);

        MyInfoClientDTO dto = new MyInfoClientDTO();
        dto.setClientId(client.getClientId());
        dto.setUserId(client.getUser().getUserId());
        dto.setDni(client.getDni());
        dto.setNames(client.getName());
        dto.setLastNames(client.getLastName());
        dto.setPhone(client.getPhone());
        dto.setHeadquarterName(client.getHeadquarter().getName());

        return dto;
    }

    @Override
    public void updateInfoAsClient(String token, Long id, DataUpdateAsClientDTO data) {
        Long clientId = Long.valueOf(jwtTokenUtil.getEntityIdFromJwt(token));
        if (!clientId.equals(id)) {
            throw new RuntimeException("No tienes permiso para modificar esta información.");
        }

        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        client.setAddress(data.getAddress());
        client.setPhone(data.getPhone());

        headquarterService.validateHeadquarterAvailable(data.getHeadquarterId());
        HeadquarterDTO hqDto = headquarterService.getHeadquarterById(data.getHeadquarterId());
        Headquarter newHeadquarter = headquarterMapper.mapToHeadquarter(hqDto);
        client.setHeadquarter(newHeadquarter);

        clientRepository.save(client);
    }

    @Override
    public void updateBlockNote(Long clientId, String blockNote) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));

        client.setBlockNote(blockNote);
        clientRepository.save(client);
    }

    //metodos para el auth
    @Override
    public Client getClientByUserForAuth(User user) {
        Client client = clientRepository.findByUser(user);
        if (client == null) {
            throw new AuthException("Cliente no encontrado", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }
        return client;
    }
}
