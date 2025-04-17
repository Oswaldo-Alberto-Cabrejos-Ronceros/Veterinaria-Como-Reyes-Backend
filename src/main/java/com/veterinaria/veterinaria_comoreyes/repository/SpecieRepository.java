package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Specie;

public interface SpecieRepository extends JpaRepository<Specie, Long> {
}
