package com.veterinaria.veterinaria_comoreyes.service.impl;


import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.repository.SpecieRepository;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecieServiceImpl implements ISpecieService {

    @Autowired
    private SpecieRepository specieRepository;

    @Override
    public SpecieDTO getSpecieById(Long id) {
        Specie specie = specieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));
        return SpecieMapper.maptoSpecieDTO(specie);
    }

    @Override
    public List<SpecieDTO> getAllSpecies() {
        return specieRepository.findAll().stream()
                .map(SpecieMapper::maptoSpecieDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SpecieDTO createSpecie(SpecieDTO specieDTO) {
        Specie specie = SpecieMapper.maptoSpecie(specieDTO);
        Specie savedSpecie = specieRepository.save(specie);
        return SpecieMapper.maptoSpecieDTO(savedSpecie);
    }

    @Override
    public SpecieDTO updateSpecie(Long id, SpecieDTO specieDTO) {
        Specie specie = specieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));

        specie.setName(specieDTO.getName());
        specie.setImagePath(specieDTO.getImagePath());
        specie.setStatus(specieDTO.getStatus());

        Specie updated = specieRepository.save(specie);
        return SpecieMapper.maptoSpecieDTO(updated);
    }

    @Override
    public void deleteSpecie(Long id) {
        Specie specie = specieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));
        specie.setStatus(0); // 0 = inactivo
        specieRepository.save(specie);
    }
}
