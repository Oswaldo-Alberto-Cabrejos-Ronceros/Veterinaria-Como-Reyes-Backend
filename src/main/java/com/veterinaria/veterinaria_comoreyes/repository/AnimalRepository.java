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

    @Query(value = """
        SELECT 
            a.animal_id,
            a.birth_date,
            a.gender,
            a.name,
            a.url_image,
            a.weight,
            b.name AS breed_name,
            s.name AS species_name,
            a.animal_comment
        FROM animal a
        INNER JOIN breed b ON a.breed_id = b.breed_id
        INNER JOIN specie s ON b.id_specie = s.specie_id
        WHERE a.client_id = :clientId AND a.status = 1
        """, nativeQuery = true)
    List<Object[]> findAnimalInfoRawByClientIdForPanel(@Param("clientId") Long clientId);



}
