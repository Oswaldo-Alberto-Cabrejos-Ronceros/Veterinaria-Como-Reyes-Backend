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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreedServiceImpl implements IBreedService {

    private final BreedRepository breedRepository;
    private final SpecieRepository specieRepository;

    @Autowired
    public BreedServiceImpl(BreedRepository breedRepository, SpecieRepository specieRepository) {
        this.breedRepository = breedRepository;
        this.specieRepository = specieRepository;
    }

    @Override
    public BreedDTO getBreedById(Long id) {
        Breed breed = breedRepository.findById(id).orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        return BreedMapper.maptoBreedDTO(breed);
    }

    @Override
    public List<BreedDTO> getBreedsBySpecies(Long speciesId) {
        Specie specie = specieRepository.findById(speciesId).orElseThrow( () -> new RuntimeException("Breed not found with id: " + speciesId));
        return breedRepository.findBySpecie(specie).stream().map(BreedMapper::maptoBreedDTO).collect(Collectors.toList());
    }

    @Override
    public List<BreedDTO> getAllBreeds() {
        return breedRepository.findAll().stream().map(BreedMapper::maptoBreedDTO).collect(Collectors.toList());
    }

    @Override
    public BreedDTO createBreed(BreedDTO breedDTO) {
        Breed breedSave = breedRepository.save(BreedMapper.maptoBreed(breedDTO));
        return BreedMapper.maptoBreedDTO(breedSave);
    }

    @Override
    public BreedDTO updateBreed(Long id, BreedDTO breedDTO) {
        Breed breed = breedRepository.findById(id).orElseThrow(() -> new RuntimeException("Breed not found with id: " + id));
        breed.setName(breedDTO.getName());
        breed.setSpecie(breedDTO.getSpecie());
        breed.setStatus(breedDTO.getStatus());
        Breed breedUpdated = breedRepository.save(breed);
        return BreedMapper.maptoBreedDTO(breedUpdated);
    }

    @Override
    public void deleteBreed(Long id) {
        Breed breed = breedRepository.findById(id).orElseThrow(() -> new RuntimeException("Breed not found with id:" + id));
        breed.setStatus((byte) 0); // inactivo
        breedRepository.save(breed);
    }
}
