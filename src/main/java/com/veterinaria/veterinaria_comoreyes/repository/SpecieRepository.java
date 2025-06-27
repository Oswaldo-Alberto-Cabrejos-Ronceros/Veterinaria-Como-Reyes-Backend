package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.Optional;

public interface SpecieRepository extends JpaRepository<Specie, Long> {

    Optional<Specie> findBySpecieIdAndStatusIsTrue(Long id);

    @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO(" +
            "s.specieId, s.name, s.imagePath, s.status) " +
            "FROM Specie s WHERE " +
            "(:name IS NULL OR s.name LIKE %:name%) AND " +
            "(:imagePath IS NULL OR s.imagePath LIKE %:imagePath%) AND " +
            "(:status IS NULL OR s.status = :status)")
    Page<SpecieDTO> searchSpecies(@Param("name") String name,
            @Param("imagePath") String imagePath,
            @Param("status") Boolean status,
            Pageable pageable);

}
