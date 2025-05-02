package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByClient(Client client);
    Optional<Animal> findByAnimalIdAndStatusIsTrue(Long id);
}
