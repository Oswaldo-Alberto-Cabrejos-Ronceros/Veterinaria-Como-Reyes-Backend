package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterRepository;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.*;
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
    private final PhoneUtil phoneUtil;
    private final HeadquarterUtil headquarterUtil;
    private final ReniecUtil reniecUtil;
    private final HeadquarterRepository headquarterRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;

    @Autowired
    public ClientServiceImpl(
        ClientRepository clientRepository,
        HeadquarterUtil headquarterUtil,
        IUserService userService,
        PhoneUtil phoneUtil,
        ReniecUtil reniecUtil,
        HeadquarterRepository headquarterRepository,
        JwtTokenUtil jwtTokenUtil,
        ClientMapper clientMapper,
        UserMapper userMapper
    ) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
        this.headquarterRepository = headquarterRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientMapper = clientMapper;
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

    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        phoneUtil.validatePhoneAvailable(clientDTO.getPhone(), "cliente");

        if (clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Cliente ya registrado con ese DNI: " + clientDTO.getDni());
        }

        reniecUtil.validateData(clientDTO.getDni(), clientDTO.getName(), clientDTO.getLastName());

        headquarterUtil.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());

        if (clientDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("C");
            userDTO.setEmail(clientDTO.getUser().getEmail());
            userDTO.setPassword(clientDTO.getUser().getPassword());
            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = userMapper.maptoUser(savedUserDTO);
            clientDTO.setUser(savedUser);
        }

        Client client = clientMapper.mapToClient(clientDTO);
        client.setStatus(true);
        return clientMapper.mapToClientDTO(clientRepository.save(client));
    }

    @Transactional
    @Override
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findByClientId(clientId);

        phoneUtil.validatePhoneAvailable(clientDTO.getPhone(), "cliente");
        if (!existingClient.getDni().equals(clientDTO.getDni()) &&
                clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Otro cliente ya registrado con el mismo DNI: " + clientDTO.getDni());
        } else {
            reniecUtil.validateData(clientDTO.getDni(), clientDTO.getName(), clientDTO.getLastName());
        }

        headquarterUtil.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());
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

        headquarterUtil.validateHeadquarterAvailable(data.getHeadquarterId());
        Headquarter newHeadquarter = headquarterRepository.findByHeadquarterId(data.getHeadquarterId());
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
}
