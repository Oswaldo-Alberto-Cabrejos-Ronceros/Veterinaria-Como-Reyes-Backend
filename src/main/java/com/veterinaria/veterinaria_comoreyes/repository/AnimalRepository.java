package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO(" +
            "a.animalId, a.name, a.gender, a.breed.breedId, a.client.clientId, " +
            "CASE WHEN a.status = true THEN 'Activo' ELSE 'Inactivo' END) " +
            "FROM Animal a WHERE " +
            "(:name IS NULL OR a.name LIKE %:name%) AND " +
            "(:gender IS NULL OR a.gender = :gender) AND " +
            "(:breedId IS NULL OR a.breed.breedId = :breedId) AND " +
            "(:clientId IS NULL OR a.client.clientId = :clientId) AND " +
            "(:status IS NULL OR a.status = :status)")
    Page<AnimalListDTO> searchAnimals(
            @Param("name") String name,
            @Param("gender") String gender,
            @Param("breedId") String breedId,
            @Param("clientId") String clientId,
            @Param("status") Boolean status,
            Pageable pageable);

}
