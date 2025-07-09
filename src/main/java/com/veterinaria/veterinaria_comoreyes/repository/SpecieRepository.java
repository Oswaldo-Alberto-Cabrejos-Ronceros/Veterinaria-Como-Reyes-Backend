package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpecieRepository extends JpaRepository<Specie, Long> {

    Optional<Specie> findBySpecieIdAndStatusIsTrue(Long id);

    @Modifying
    @Query("UPDATE Specie s SET s.status = true WHERE s.specieId = :specieId")
    void activateSpecie(@Param("specieId") Long specieId);

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO(
                    s.specieId,
                    s.name,
                    CASE WHEN s.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM Specie s
                WHERE (:name IS NULL OR s.name LIKE %:name%) AND
                      (:status IS NULL OR s.status = :status)
            """)
    Page<SpecieListDTO> searchSpeciesWithFilters(
            @Param("name") String name,
            @Param("status") Boolean status,
            Pageable pageable);

    @Modifying
    @Query(value = "UPDATE breed SET status = 0 WHERE id_specie = :specieId", nativeQuery = true)
    void disableBreedsBySpecieId(@Param("specieId") Long specieId);

}
