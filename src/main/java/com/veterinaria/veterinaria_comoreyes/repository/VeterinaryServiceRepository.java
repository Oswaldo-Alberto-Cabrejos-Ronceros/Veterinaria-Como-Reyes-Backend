package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Service.ServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Category;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VeterinaryServiceRepository extends JpaRepository<VeterinaryService, Long> {
    List<VeterinaryService> findAllBySpecie(Specie specie);

    List<VeterinaryService> findAllByCategory(Category category);

    Optional<VeterinaryService> findByServiceIdAndStatusIsTrue(Long id);

    @Modifying
    @Query("UPDATE VeterinaryService v SET v.status = true WHERE v.serviceId = :serviceId")
    void activateVeterinaryService(@Param("serviceId") Long serviceId);

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Service.ServiceListDTO(
                    s.serviceId,
                    s.name,
                    s.specie.name,
                    s.category.name,
                    CASE WHEN s.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM VeterinaryService s
                WHERE (:name IS NULL OR s.name LIKE %:name%) AND
                      (:specie IS NULL OR s.specie.name LIKE %:specie%) AND
                      (:category IS NULL OR s.category.name LIKE %:category%) AND
                      (:status IS NULL OR s.status = :status)
            """)
    Page<ServiceListDTO> searchServicesWithFilters(
            @Param("name") String name,
            @Param("specie") String specie,
            @Param("category") String category,
            @Param("status") Boolean status,
            Pageable pageable);
}
