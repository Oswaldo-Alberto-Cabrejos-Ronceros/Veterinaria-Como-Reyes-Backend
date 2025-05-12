package com.veterinaria.veterinaria_comoreyes.service.impl;


import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.repository.SpecieRepository;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecieServiceImpl implements ISpecieService {

    private final SpecieRepository specieRepository;
    private final SpecieMapper specieMapper;

    @Autowired
    public SpecieServiceImpl(SpecieRepository specieRepository, SpecieMapper specieMapper) {
        this.specieRepository = specieRepository;
        this.specieMapper = specieMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public SpecieDTO getSpecieById(Long id) {
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));
        return specieMapper.mapToSpecieDTO(specie);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SpecieDTO> getAllSpecies() {
        return specieRepository.findAll().stream()
                .map(specieMapper::mapToSpecieDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SpecieDTO createSpecie(SpecieDTO specieDTO) {
        Specie specie = specieMapper.mapToSpecie(specieDTO);
        Specie savedSpecie = specieRepository.save(specie);
        return specieMapper.mapToSpecieDTO(savedSpecie);
    }

    @Transactional
    @Override
    public SpecieDTO updateSpecie(Long id, SpecieDTO specieDTO) {
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));

        specie.setName(specieDTO.getName());
        specie.setImagePath(specieDTO.getImagePath());

        Specie updated = specieRepository.save(specie);
        return specieMapper.mapToSpecieDTO(updated);
    }

    @Transactional
    @Override
    public void deleteSpecie(Long id) {
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));
        specie.setStatus(false);
        specieRepository.save(specie);
    }
}

