package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAnimalService {

    AnimalDTO getAnimalById(Long id);

    List<AnimalDTO> getAllAnimals();

    List<AnimalDTO> getAnimalsByClient(Long clientId);

    AnimalDTO createAnimal(AnimalDTO animalDTO);

    AnimalDTO updateAnimal(Long id, AnimalDTO animalDTO);

    void deleteAnimal(Long id);

    void validateAnimalExistAndStatus(Long id);

    void validateClientExistAndStatusForAnimalId(Long id);

    String findSpecieNameByAnimalId(Long id);

    Page<AnimalListDTO> searchAnimals(String name, String gender, String breedId, String clientId, Boolean status,
            Pageable pageable);

    List<AnimalInfoForClientDTO> getAnimalsByClientId(Long clientId);
}