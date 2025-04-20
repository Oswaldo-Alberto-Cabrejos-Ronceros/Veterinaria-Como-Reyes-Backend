package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;

import java.util.List;

public interface IClientService {

    ClientDTO getClientById(Long id);

    ClientDTO getClientByUser(UserDTO user);

    List<ClientDTO> getAllClients();

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(Long id,ClientDTO clientDTO);

    void deleteClientById(Long id);

}
