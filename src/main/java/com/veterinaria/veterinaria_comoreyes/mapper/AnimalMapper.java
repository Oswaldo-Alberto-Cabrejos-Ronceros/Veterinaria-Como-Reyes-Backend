package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;

public class AnimalMapper {
    public static Animal maptoAnimal(AnimalDTO animalDTO) {
        return new Animal(
                animalDTO.getAnimalId(),
                animalDTO.getName(),
                animalDTO.getGender(),
                animalDTO.getWeight(),
                animalDTO.getBirthDate(),
                animalDTO.getComment(),
                animalDTO.getUrlImage(),
                animalDTO.getBreed(),
                animalDTO.getClient(),
                animalDTO.getStatus()
        );
    }

    public static AnimalDTO maptoAnimalDTO(Animal animal) {
        return new AnimalDTO(
                animal.getAnimalId(),
                animal.getName(),
                animal.getGender(),
                animal.getWeight(),
                animal.getBirthDate(),
                animal.getComment(),
                animal.getUrlImage(),
                animal.getBreed(),
                animal.getClient(),
                animal.getStatus()
        );
    }
}
