package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalDTO;

import java.util.List;

public interface IAnimalService {

    AnimalDTO getAnimalById(Long id);

    List<AnimalDTO> getAllAnimals();

    List<AnimalDTO> getAnimalsByClient(Long clientId);

    AnimalDTO createAnimal(AnimalDTO animalDTO);

    AnimalDTO updateAnimal(Long id, AnimalDTO animalDTO);

    void deleteAnimal(Long id);

}
