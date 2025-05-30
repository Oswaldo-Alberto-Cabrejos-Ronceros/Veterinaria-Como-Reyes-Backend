package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = GlobalMapperConfig.class,
        componentModel = "spring")
public interface AnimalMapper {

    @Mapping(source = "clientId", target = "client", qualifiedByName = "mapClient")
    Animal mapToAnimal(AnimalDTO dto);

    @Mapping(source = "client.clientId", target = "clientId")
    AnimalDTO mapToAnimalDTO(Animal animal);

    @Named("mapClient")
    static Client mapClient(Long id) {
        Client client = new Client();
        client.setClientId(id);
        return client;
    }
}
