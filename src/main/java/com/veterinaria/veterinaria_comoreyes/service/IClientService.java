package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Client.*;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.User;
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
    void updateInfoAsClient(String Token, Long id, DataUpdateAsClientDTO dataUpdateAsClientDTO);

    // SERVICES OF CLIENT TO AS AUTH

    Client getClientByUserForAuth(User user);

    void validateClientExistsAndStatus(Long clientId);

    nMyInfoClientDTO getMyInfoAsClient(Long id);

    ClientBasicInfoByDniDTO getClientBasicInfoByDni(String dni);

    List<ClientInfoPanelAdminDTO> getClientInfoPanelAdmin();

    List<ClientInfoPanelAdminDTO> getClientInfoPanelByHeadquarterManager(Long headquarterId);
}