package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final IHeadquarterService headquarterService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, IHeadquarterService headquarterService) {
        this.clientRepository = clientRepository;
        this.headquarterService = headquarterService;
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        return ClientMapper.mapToClientDTO(client);
    }

    @Override
    public ClientDTO getClientByUser(UserDTO userDTO) {
        Client client = clientRepository.findByUser(UserMapper.maptoUser(userDTO));
        return ClientMapper.mapToClientDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(ClientMapper::mapToClientDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = ClientMapper.mapToClient(clientDTO);
        HeadquarterDTO headquarterDTO = headquarterService.getHeadquarterById(client.getHeadquarter().getHeadquarterId());
        Headquarter headquarter = HeadquarterMapper.maptoHeadquarter(headquarterDTO);
        client.setHeadquarter(headquarter);
        Client savedClient = clientRepository.save(client);
        return ClientMapper.mapToClientDTO(savedClient);
    }

    @Transactional
    @Override
    public ClientDTO updateClient(Long id,ClientDTO clientDTO) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        client.setName(clientDTO.getName());
        client.setLastName(clientDTO.getLastName());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        client.setHeadquarter(clientDTO.getHeadquarter());
        client.setDirImage(clientDTO.getDirImage());
        client.setStatus(clientDTO.getStatus());
        Client savedClient = clientRepository.save(client);
        return ClientMapper.mapToClientDTO(savedClient);
    }

    @Transactional
    @Override
    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        client.setStatus((byte) 0);
        clientRepository.save(client);
    }
}
