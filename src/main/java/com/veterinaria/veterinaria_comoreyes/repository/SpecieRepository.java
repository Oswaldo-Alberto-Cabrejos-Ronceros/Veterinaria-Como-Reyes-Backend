package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import java.util.Optional;

public interface SpecieRepository extends JpaRepository<Specie, Long> {

    Optional<Specie> findByIdAndStatus(Long id);
}
