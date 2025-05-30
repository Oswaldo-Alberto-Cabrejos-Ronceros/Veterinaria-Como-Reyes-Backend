package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class,
        componentModel = "spring")
public interface ClientMapper {
    
    ClientDTO mapToClientDTO(Client client);

    @Mapping(target = "clientId", ignore = true)
    Client mapToClient(ClientDTO clientDTO);
}
