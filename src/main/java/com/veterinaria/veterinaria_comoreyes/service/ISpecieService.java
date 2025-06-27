package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISpecieService {
    SpecieDTO getSpecieById(Long id);

    List<SpecieDTO> getAllSpecies();

    SpecieDTO createSpecie(SpecieDTO specieDTO);

    SpecieDTO updateSpecie(Long id, SpecieDTO specieDTO);

    void deleteSpecie(Long id);

    Page<SpecieDTO> searchSpecies(String name, String imagePath, Boolean status, Pageable pageable);

}