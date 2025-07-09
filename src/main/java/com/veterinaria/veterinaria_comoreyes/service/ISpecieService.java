package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ISpecieService {
    SpecieDTO getSpecieById(Long id);

    List<SpecieDTO> getAllSpecies();

    SpecieDTO createSpecie(SpecieDTO specieDTO);

    SpecieDTO updateSpecie(Long id, SpecieDTO specieDTO);

    void deleteSpecie(Long id);

    @Transactional
    void activateSpecie(Long specieId);

    Page<SpecieListDTO> searchSpecies(String name, Boolean status, Pageable pageable);

    TopSpeciesByAppointmentsDTO getTopSpeciesGeneral();

    TopSpeciesByAppointmentsDTO getTopSpeciesByHeadquarter(Long headquarterId);
}