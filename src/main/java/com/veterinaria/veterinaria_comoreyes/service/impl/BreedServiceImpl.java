package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.BreedDTO;
import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.BreedMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.repository.BreedRepository;
import com.veterinaria.veterinaria_comoreyes.repository.SpecieRepository;
import com.veterinaria.veterinaria_comoreyes.service.IBreedService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreedServiceImpl implements IBreedService {

    private final BreedRepository breedRepository;
    private final SpecieRepository specieRepository;
    private final FilterStatus filterStatus;

    @Autowired
    public BreedServiceImpl(BreedRepository breedRepository, SpecieRepository specieRepository,FilterStatus filterStatus) {
        this.breedRepository = breedRepository;
        this.specieRepository = specieRepository;
        this.filterStatus = filterStatus;
    }

    @Transactional(readOnly = true)
    @Override
    public BreedDTO getBreedById(Long id) {
        filterStatus.activeFilterStatus(true);
        Breed breed = breedRepository.findByIdAndStatusIsTrue(id).orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        return BreedMapper.maptoBreedDTO(breed);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BreedDTO> getBreedsBySpecies(Long speciesId) {
        filterStatus.activeFilterStatus(true);
        Specie specie = specieRepository.findByIdAndStatus(speciesId).orElseThrow( () -> new RuntimeException("Breed not found with id: " + speciesId));
        return breedRepository.findBySpecie(specie).stream().map(BreedMapper::maptoBreedDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BreedDTO> getAllBreeds() {
        filterStatus.activeFilterStatus(true);
        return breedRepository.findAll().stream().map(BreedMapper::maptoBreedDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BreedDTO createBreed(BreedDTO breedDTO) {
        filterStatus.activeFilterStatus(true);
        Breed breedSave = breedRepository.save(BreedMapper.maptoBreed(breedDTO));
        return BreedMapper.maptoBreedDTO(breedSave);
    }

    @Transactional
    @Override
    public BreedDTO updateBreed(Long id, BreedDTO breedDTO) {
        Breed breed = breedRepository.findByIdAndStatusIsTrue(id).orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        breed.setName(breedDTO.getName());
        breed.setSpecie(breedDTO.getSpecie());
        Breed breedUpdated = breedRepository.save(breed);
        return BreedMapper.maptoBreedDTO(breedUpdated);
    }

    @Transactional
    @Override
    public void deleteBreed(Long id) {
        Breed breed = breedRepository.findById(id).orElseThrow(() -> new RuntimeException("Breed not found with id:" + id));
        breed.setStatus(false); // inactivo
        breedRepository.save(breed);
    }
}
