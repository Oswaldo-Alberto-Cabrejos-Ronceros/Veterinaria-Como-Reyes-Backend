package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;

import java.util.List;

public interface ISpecieService {
    SpecieDTO getSpecieById(Long id);

    List<SpecieDTO> getAllSpecies();

    SpecieDTO createSpecie(SpecieDTO specieDTO);

    SpecieDTO updateSpecie(Long id, SpecieDTO specieDTO);

    void deleteSpecie(Long id);
}