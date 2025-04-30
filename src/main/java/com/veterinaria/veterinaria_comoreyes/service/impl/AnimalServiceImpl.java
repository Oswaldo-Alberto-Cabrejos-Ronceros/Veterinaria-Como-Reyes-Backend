package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.mapper.AnimalMapper;
import com.veterinaria.veterinaria_comoreyes.repository.AnimalRepository;
import com.veterinaria.veterinaria_comoreyes.repository.BreedRepository;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.service.IAnimalService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalServiceImpl implements IAnimalService {

    private final AnimalRepository animalRepository;
    private final ClientRepository clientRepository;
    private final BreedRepository breedRepository;
    private final FilterStatus filterStatus;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository, ClientRepository clientRepository, BreedRepository breedRepository,FilterStatus filterStatus) {
        this.animalRepository = animalRepository;
        this.clientRepository = clientRepository;
        this.breedRepository = breedRepository;
        this.filterStatus = filterStatus;
    }

    @Transactional(readOnly = true)
    @Override
    public AnimalDTO getAnimalById(Long id) {
        filterStatus.activeFilterStatus(true);
        return AnimalMapper.maptoAnimalDTO(animalRepository.findByIdAndStatusIsTrue(id).orElseThrow(()->new RuntimeException("Animal not found with id:" + id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AnimalDTO> getAllAnimals() {
        filterStatus.activeFilterStatus(true);
        return animalRepository.findAll().stream().map(AnimalMapper::maptoAnimalDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AnimalDTO> getAnimalsByClient(Long clientId) {
        filterStatus.activeFilterStatus(true);
        Client client = clientRepository.findById(clientId).orElseThrow(()->new RuntimeException("Client not found with id:" + clientId));
        return animalRepository.findByClient(client).stream().map(AnimalMapper::maptoAnimalDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {
        filterStatus.activeFilterStatus(true);
        //buscar cliente asociado
        Animal animal = AnimalMapper.maptoAnimal(animalDTO);
        return AnimalMapper.maptoAnimalDTO(animalRepository.save(animal));
    }

    @Transactional
    @Override
    public AnimalDTO updateAnimal(Long id, AnimalDTO animalDTO) {
        filterStatus.activeFilterStatus(true);
        Animal animal = animalRepository.findByIdAndStatusIsTrue(id).orElseThrow(()->new RuntimeException("Animal not found with id:" + id));
        //validar cliente
        animal.setName(animalDTO.getName());
        animal.setBreed(animalDTO.getBreed());
        animal.setGender(animalDTO.getGender());
        animal.setAnimalComment(animalDTO.getAnimalComment());
        animal.setBirthDate(animalDTO.getBirthDate());
        animal.setWeight(animalDTO.getWeight());
        animal.setUrlImage(animalDTO.getUrlImage());
        return AnimalMapper.maptoAnimalDTO(animalRepository.save(animal));
    }

    @Transactional
    @Override
    public void deleteAnimal(Long id) {
        filterStatus.activeFilterStatus(true);
        Animal animal = animalRepository.findByIdAndStatusIsTrue(id).orElseThrow(()->new RuntimeException("Animal not found with id:" + id));
        animal.setStatus(false); //inactivo
        animalRepository.save(animal);
    }
}
