package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
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
