package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {

    // Metodo para saber si existe id con esa sede
    boolean existsByHeadquarterId(long headquarterId);

    // Método para obtener solo el estado (status) de la sede por su id
    @Query("SELECT h.status FROM Headquarter h WHERE h.headquarterId = :id")
    Byte findStatusById(@Param("id") long id);
}
