package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface ClientMapper {
    
    ClientDTO mapToClientDTO(Client client);

    Client mapToClient(ClientDTO clientDTO);
}
