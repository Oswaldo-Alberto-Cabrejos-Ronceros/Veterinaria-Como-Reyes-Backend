package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.BreedDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;

public class BreedMapper {
    public static BreedDTO maptoBreedDTO(Breed breed){
        return new BreedDTO(
          breed.getBreedId(),
          breed.getSpecie(),
          breed.getName()
        );
    }

    public static Breed maptoBreed(BreedDTO breedDTO){
        return new Breed(
                breedDTO.getBreedId(),
                breedDTO.getSpecie(),
                breedDTO.getName()
        );
    }
}
