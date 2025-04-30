package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;

public class AnimalMapper {
    public static Animal maptoAnimal(AnimalDTO animalDTO) {
        Client client = new Client();
        client.setClientId(animalDTO.getClientId());
        return new Animal(
                animalDTO.getAnimalId(),
                animalDTO.getName(),
                animalDTO.getGender(),
                animalDTO.getWeight(),
                animalDTO.getBirthDate(),
                animalDTO.getAnimalComment(),
                animalDTO.getUrlImage(),
                animalDTO.getBreed(),
                client
        );
    }

    public static AnimalDTO maptoAnimalDTO(Animal animal) {
        return new AnimalDTO(
                animal.getAnimalId(),
                animal.getName(),
                animal.getGender(),
                animal.getWeight(),
                animal.getBirthDate(),
                animal.getAnimalComment(),
                animal.getUrlImage(),
                animal.getBreed(),
                animal.getClient().getClientId()
        );
    }
}
