package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.BreedMapper;
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
    private final BreedMapper breedMapper;

    @Autowired
    public BreedServiceImpl(BreedRepository breedRepository, SpecieRepository specieRepository, FilterStatus filterStatus, BreedMapper breedMapper) {
        this.breedRepository = breedRepository;
        this.specieRepository = specieRepository;
        this.filterStatus = filterStatus;
        this.breedMapper = breedMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public BreedDTO getBreedById(Long id) {
        filterStatus.activeFilterStatus(true);
        Breed breed = breedRepository.findByBreedIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        return breedMapper.maptoBreedDTO(breed);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BreedDTO> getBreedsBySpecies(Long speciesId) {
        filterStatus.activeFilterStatus(true);
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(speciesId)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + speciesId));
        return breedRepository.findBySpecie(specie)
                .stream()
                .map(breedMapper::maptoBreedDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BreedDTO> getAllBreeds() {
        filterStatus.activeFilterStatus(true);
        return breedRepository.findAll()
                .stream()
                .map(breedMapper::maptoBreedDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BreedDTO createBreed(BreedDTO breedDTO) {
        Long specieId = breedDTO.getSpecie().getSpecieId();

        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(specieId)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + specieId));

        Breed breed = breedMapper.maptoBreed(breedDTO);
        breed.setStatus(true);
        breed.setSpecie(specie);
        return breedMapper.maptoBreedDTO(breedRepository.save(breed));
    }

    @Transactional
    @Override
    public BreedDTO updateBreed(Long id, BreedDTO breedDTO) {
        Breed breed = breedRepository.findByBreedIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));

        // Validar especie
        Long specieId = breedDTO.getSpecie().getSpecieId();
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(specieId)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + specieId));

        breed.setName(breedDTO.getName());
        breed.setSpecie(specie);

        return breedMapper.maptoBreedDTO(breedRepository.save(breed));
    }

    @Transactional
    @Override
    public void deleteBreed(Long id) {
        Breed breed = breedRepository.findByBreedIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        breed.setStatus(false);
        breedRepository.save(breed);
    }
}
