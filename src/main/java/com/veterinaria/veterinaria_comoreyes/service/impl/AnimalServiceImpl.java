package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.mapper.AnimalMapper;
import com.veterinaria.veterinaria_comoreyes.repository.AnimalRepository;
import com.veterinaria.veterinaria_comoreyes.repository.BreedRepository;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.service.IAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalServiceImpl implements IAnimalService {

    private final AnimalRepository animalRepository;
    private final ClientRepository clientRepository;
    private final BreedRepository breedRepository;

    @Autowired
    public AnimalServiceImpl(AnimalRepository animalRepository, ClientRepository clientRepository, BreedRepository breedRepository) {
        this.animalRepository = animalRepository;
        this.clientRepository = clientRepository;
        this.breedRepository = breedRepository;
    }

    @Override
    public AnimalDTO getAnimalById(Long id) {
        return AnimalMapper.maptoAnimalDTO(animalRepository.findById(id).orElseThrow(()->new RuntimeException("Animal not found with id:" + id)));
    }

    @Override
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository.findAll().stream().map(AnimalMapper::maptoAnimalDTO).collect(Collectors.toList());
    }

    @Override
    public List<AnimalDTO> getAnimalsByClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(()->new RuntimeException("Client not found with id:" + clientId));
        return animalRepository.findByClient(client).stream().map(AnimalMapper::maptoAnimalDTO).collect(Collectors.toList());
    }

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {
        Animal animal = AnimalMapper.maptoAnimal(animalDTO);
        return AnimalMapper.maptoAnimalDTO(animalRepository.save(animal));
    }

    @Override
    public AnimalDTO updateAnimal(Long id, AnimalDTO animalDTO) {
        Animal animal = animalRepository.findById(id).orElseThrow(()->new RuntimeException("Animal not found with id:" + id));
        animal.setName(animalDTO.getName());
        animal.setBreed(animalDTO.getBreed());
        animal.setGender(animalDTO.getGender());
        animal.setAnimalComment(animalDTO.getAnimalComment());
        animal.setBirthDate(animalDTO.getBirthDate());
        animal.setWeight(animalDTO.getWeight());
        animal.setUrlImage(animalDTO.getUrlImage());
        animal.setStatus(animalDTO.getStatus());
        return AnimalMapper.maptoAnimalDTO(animalRepository.save(animal));
    }

    @Override
    public void deleteAnimal(Long id) {
        Animal animal = animalRepository.findById(id).orElseThrow(()->new RuntimeException("Animal not found with id:" + id));
        animal.setStatus((byte) 0); //inactivo
        animalRepository.save(animal);
    }
}
