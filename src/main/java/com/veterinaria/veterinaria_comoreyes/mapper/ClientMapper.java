package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.AuthenticationResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;

public class ClientMapper {
    public static ClientDTO mapToClientDTO(Client client) {
        return new ClientDTO(
        client.getClientId(),
                client.getDni(),
                client.getName(),
                client.getLastName(),
                client.getAddress(),
                client.getPhone(),
                client.getBirthDate(),
                client.getDirImage(),
                client.getHeadquarter(),
                client.getUser(),
                client.getBlockNote()
        );
    }

    public static Client mapToClient(ClientDTO clientDTO) {
        return new Client(
                clientDTO.getClientId(),
                clientDTO.getDni(),
                clientDTO.getName(),
                clientDTO.getLastName(),
                clientDTO.getAddress(),
                clientDTO.getPhone(),
                clientDTO.getBirthDate(),
                clientDTO.getDirImage(),
                clientDTO.getHeadquarter(),
                clientDTO.getUser(),
                clientDTO.getBlockNote()
        );
    }

    public static AuthenticationResponseDTO mapToAuthenticationResponseDTO(Long userId,ClientDTO clientDTO,String jwtToken, String refreshToken) {
        return new AuthenticationResponseDTO(userId,clientDTO.getName(),"CLIENT",jwtToken,refreshToken);
    }
}
