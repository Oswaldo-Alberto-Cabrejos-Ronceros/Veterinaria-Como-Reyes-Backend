package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedDTO;

import java.util.List;

import org.springframework.data.domain.Page;

public interface IBreedService {
    BreedDTO getBreedById(Long id);

    List<BreedDTO> getBreedsBySpecies(Long speciesId);

    List<BreedDTO> getAllBreeds();

    BreedDTO createBreed(BreedDTO breedDTO);

    BreedDTO updateBreed(Long id, BreedDTO breedDTO);

    void deleteBreed(Long id);

    Page<BreedDTO> getBreedsBySpeciePaginated(Long specieId, int page, int size);

}
