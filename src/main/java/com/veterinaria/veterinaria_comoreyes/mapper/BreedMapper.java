package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class,
        componentModel = "spring")
public interface BreedMapper {

    BreedDTO maptoBreedDTO(Breed breed);

    Breed maptoBreed(BreedDTO breedDTO);
}
