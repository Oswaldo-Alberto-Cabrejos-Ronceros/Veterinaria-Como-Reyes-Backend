package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import java.util.Optional;

public interface SpecieRepository extends JpaRepository<Specie, Long> {

        Optional<Specie> findBySpecieIdAndStatusIsTrue(Long id);

        @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO(" +
                        "s.specieId, s.name, s.imagePath, s.status) " +
                        "FROM Specie s WHERE " +
                        "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                        "(:imagePath IS NULL OR LOWER(s.imagePath) LIKE LOWER(CONCAT('%', :imagePath, '%'))) AND " +
                        "(:status IS NULL OR s.status = :status)")
        Page<SpecieListDTO> searchSpecies(@Param("name") String name,
                        @Param("imagePath") String imagePath,
                        @Param("status") Boolean status,
                        Pageable pageable);

}
