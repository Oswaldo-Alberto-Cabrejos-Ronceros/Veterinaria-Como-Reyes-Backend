package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Service.VetServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Category;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;

import io.lettuce.core.dynamic.annotation.Param;

import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VeterinaryServiceRepository extends JpaRepository<VeterinaryService, Long> {
    List<VeterinaryService> findAllBySpecie(Specie specie);

    List<VeterinaryService> findAllByCategory(Category category);

    Optional<VeterinaryService> findByServiceIdAndStatusIsTrue(Long id);

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Service.VetServiceListDTO(
                    v.serviceId,
                    v.name,
                    v.price,
                    CAST(v.specie.specieId AS string),
                    CAST(v.category.categoryId AS string),
                    CASE WHEN v.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM VeterinaryService v
                WHERE (:name IS NULL OR v.name LIKE %:name%)
                  AND (:price IS NULL OR v.price = :price)
                  AND (:specieId IS NULL OR CAST(v.specie.specieId AS string) = :specieId)
                  AND (:categoryId IS NULL OR CAST(v.category.categoryId AS string) = :categoryId)
                  AND (:status IS NULL OR v.status = :status)
            """)
    Page<VetServiceListDTO> searchVetServices(
            @Param("name") String name,
            @Param("price") Double price,
            @Param("specieId") String specieId,
            @Param("categoryId") String categoryId,
            @Param("status") Boolean status,
            Pageable pageable);
}
