package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Breed;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    List<Breed> findBySpecie(Specie specie);
    Optional<Breed> findByBreedIdAndStatusIsTrue(Long id);
}
