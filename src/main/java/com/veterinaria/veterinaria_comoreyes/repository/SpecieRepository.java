package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
