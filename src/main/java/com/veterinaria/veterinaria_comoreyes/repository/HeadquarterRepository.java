package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;

import java.util.Optional;

public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {
Optional<Headquarter> findByIdAndStatusIsTrue(Long id);
}
