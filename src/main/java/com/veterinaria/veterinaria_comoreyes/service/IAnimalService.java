package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    List<AnimalInfoForClientDTO> getAnimalsByClientId(Long clientId);

    @Transactional
    void activateAnimal(Long animalId);

    Page<AnimalListDTO> searchAnimals(String name, String owner,
            String specie, String breed,
            String gender, Boolean status,
            Pageable pageable);

    AnimalDTO updateAnimalWeight(Long id, Float weight);

}
