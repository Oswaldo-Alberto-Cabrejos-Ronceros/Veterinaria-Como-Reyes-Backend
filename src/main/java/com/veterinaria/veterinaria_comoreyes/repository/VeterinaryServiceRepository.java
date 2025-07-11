package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Service.ServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.ServicesInfoTopPanelAdminDTO;
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
                    s.price,
                    s.duration,
                    CASE WHEN s.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM VeterinaryService s
                WHERE (:name IS NULL OR s.name LIKE %:name%) AND
                      (:specie IS NULL OR s.specie.name LIKE %:specie%) AND
                      (:category IS NULL OR s.category.name LIKE %:category%) AND
                      (:status IS NULL OR s.status = :status)
                ORDER BY s.serviceId DESC
            """)
    Page<ServiceListDTO> searchServicesWithFilters(
            @Param("name") String name,
            @Param("specie") String specie,
            @Param("category") String category,
            @Param("status") Boolean status,
            Pageable pageable);

    @Query(value = """
                SELECT
                    vs.service_id AS service_id,
                    vs.name AS service_name,
                    c.name AS category_name,
                    vs.dir_image AS image_url,
                    COUNT(*) AS total_cares
                FROM
                    care ca
                JOIN
                    headquarter_vet_service hvs ON ca.headquarter_vetservice_id = hvs.id
                JOIN
                    veterinary_service vs ON hvs.id_service = vs.service_id
                JOIN
                    category c ON vs.id_category = c.category_id
                GROUP BY
                    vs.dir_image, vs.name, c.name, vs.service_id
                ORDER BY
                    total_cares DESC
            """, nativeQuery = true)
    List<Object[]> getTopServicesPanelAdmin();

    @Query(value = """
                SELECT
                    vs.service_id AS service_id,
                    vs.name AS service_name,
                    c.name AS category_name,
                    vs.dir_image AS image_url,
                    COUNT(*) AS total_cares
                FROM
                    care ca
                JOIN
                    headquarter_vet_service hvs ON ca.headquarter_vetservice_id = hvs.id
                JOIN
                    veterinary_service vs ON hvs.id_service = vs.service_id
                JOIN
                    category c ON vs.id_category = c.category_id
                WHERE
                    hvs.id_headquarter = :headquarterId
                GROUP BY
                     vs.dir_image, vs.service_id, vs.name, c.name
                ORDER BY
                    total_cares DESC
            """, nativeQuery = true)
    List<Object[]> getTopServicesPanelManager(@Param("headquarterId") Long headquarterId);

}
