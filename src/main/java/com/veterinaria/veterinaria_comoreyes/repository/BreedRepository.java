package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    List<Breed> findBySpecie(Specie specie);

    Optional<Breed> findByBreedIdAndStatusIsTrue(Long id);

    @Modifying
    @Query("UPDATE Breed b SET b.status = true WHERE b.breedId = :breedId")
    void activateBreed(@Param("breedId") Long breedId);

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedListDTO(
                    b.breedId,
                    b.name,
                    b.specie.name,
                    CASE WHEN b.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM Breed b
                WHERE (:name IS NULL OR b.name LIKE %:name%) AND
                      (:specieName IS NULL OR b.specie.name LIKE %:specieName%) AND
                      (:status IS NULL OR b.status = :status)
            """)
    Page<BreedListDTO> searchBreedsWithFilters(
            @Param("name") String name,
            @Param("specieName") String specieName,
            @Param("status") Boolean status,
            Pageable pageable);
}
