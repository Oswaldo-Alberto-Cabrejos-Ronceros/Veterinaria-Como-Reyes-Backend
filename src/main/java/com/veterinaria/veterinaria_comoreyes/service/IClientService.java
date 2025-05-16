package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClientService {

    Page<ClientListDTO> searchClients(String dni, String name, String lastName, Boolean status, Long headquarterId, Pageable pageable);

    ClientDTO getClientById(Long id);
    ClientDTO getClientByUser(UserDTO userDTO);
    List<ClientDTO> getAllClients();
    ClientDTO createClient(ClientDTO clientDTO);
    ClientDTO updateClient(Long id, ClientDTO clientDTO);
    void blockClientById(Long id);
    void deleteClientById(Long id);
    void updateBlockNote(Long id, String blockNote);


    // SERVICES OF CLIENT TO AS A USER
    MyInfoClientDTO myInfoAsClient(String Token, Long id);
    void updateInfoAsClient(String Token, Long id, DataUpdateAsClientDTO dataUpdateAsClientDTO);

    // SERVICES OF CLIENT TO AS AUTH

    Client getClientByUserForAuth(User user);

}