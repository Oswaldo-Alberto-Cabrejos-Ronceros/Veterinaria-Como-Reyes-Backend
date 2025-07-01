package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
                s.specie_id,
                b.name AS breed_name,
                s.name AS species_name,
                a.animal_comment
            FROM animal a
            INNER JOIN breed b ON a.breed_id = b.breed_id
            INNER JOIN specie s ON b.id_specie = s.specie_id
            WHERE a.client_id = :clientId AND a.status = 1
            """, nativeQuery = true)
    List<Object[]> findAnimalInfoRawByClientIdForPanel(@Param("clientId") Long clientId);

    @Modifying
    @Query(value = "UPDATE animal SET status = 1 WHERE animal_id = :animalId", nativeQuery = true)
    void activateAnimal(@Param("animalId") Long animalId);

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO(
                    a.animalId,
                    a.name,
                    a.client.name,
                    a.breed.specie.name,
                    a.breed.name,
                    a.gender,
                    CASE WHEN a.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM Animal a
                WHERE (:name IS NULL OR a.name LIKE %:name%) AND
                      (:owner IS NULL OR a.client.name LIKE %:owner%) AND
                      (:specie IS NULL OR a.breed.specie.name LIKE %:specie%) AND
                      (:breed IS NULL OR a.breed.name LIKE %:breed%) AND
                      (:gender IS NULL OR a.gender = :gender) AND
                      (:status IS NULL OR a.status = :status)
            """)
    Page<AnimalListDTO> searchAnimalsWithFullDetails(
            @Param("name") String name,
            @Param("owner") String owner,
            @Param("specie") String specie,
            @Param("breed") String breed,
            @Param("gender") String gender,
            @Param("status") Boolean status,
            Pageable pageable);

}
