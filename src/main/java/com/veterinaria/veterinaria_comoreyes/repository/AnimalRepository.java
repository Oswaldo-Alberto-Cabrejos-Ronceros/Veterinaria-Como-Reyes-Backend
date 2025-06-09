package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByClient(Client client);
    Optional<Animal> findByAnimalIdAndStatusIsTrue(Long id);

    boolean existsByAnimalIdAndStatusIsTrue(long id);


    @Query("SELECT a.client.clientId FROM Animal a WHERE a.animalId = :id")
    Long clientIdForAnimalId(@Param("id") Long id);

    @Query("SELECT a.breed.specie.name FROM Animal a WHERE a.animalId = :id")
    Optional<String> findSpecieNameByAnimalId(@Param("id") Long id);



}
