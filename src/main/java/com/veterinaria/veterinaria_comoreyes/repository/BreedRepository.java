package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedListBySpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {
        List<Breed> findBySpecie(Specie specie);

        Optional<Breed> findByBreedIdAndStatusIsTrue(Long id);

        @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedListBySpecieDTO(" +
                        "b.breedId, b.specie.name, CASE WHEN b.status = true THEN 'Activo' ELSE 'Bloqueado' END) " +
                        "FROM Breed b " +
                        "WHERE (:specieName IS NULL OR b.specie.name LIKE %:specieName%) " +
                        "AND (:status IS NULL OR b.status = :status)")
        Page<BreedListBySpecieDTO> searchBreedBySpecieName(
                        @Param("specieName") String specieName,
                        @Param("status") Boolean status,
                        Pageable pageable);
}
