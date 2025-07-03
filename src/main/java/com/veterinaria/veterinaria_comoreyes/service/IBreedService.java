package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBreedService {
    BreedDTO getBreedById(Long id);

    List<BreedDTO> getBreedsBySpecies(Long speciesId);

    List<BreedDTO> getAllBreeds();

    BreedDTO createBreed(BreedDTO breedDTO);

    BreedDTO updateBreed(Long id, BreedDTO breedDTO);

    void deleteBreed(Long id);

    @Transactional
    void activateBreed(Long breedId);

    Page<BreedListDTO> searchBreeds(String name, String specieName, Boolean status, Pageable pageable);
}
