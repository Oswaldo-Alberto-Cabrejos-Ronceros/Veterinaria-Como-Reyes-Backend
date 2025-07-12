package com.veterinaria.veterinaria_comoreyes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.HeadquarterVetService;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeadquarterVetServiceRepository extends JpaRepository<HeadquarterVetService, Long> {
    Optional<HeadquarterVetService> findByIdAndStatusIsTrue(Long id);

    boolean existsByHeadquarterAndVeterinaryService(Headquarter headquarter, VeterinaryService veterinaryService);

    List<HeadquarterVetService> findByVeterinaryService(VeterinaryService veterinaryService);

    List<HeadquarterVetService> findByHeadquarter(Headquarter headquarter);

    boolean existsByIdAndStatusIsTrue(Long id);

    //Todos los estados
    List<HeadquarterVetService> findByHeadquarter_HeadquarterId(Long headquarterId);

    @Query("SELECT hs.veterinaryService.name FROM HeadquarterVetService hs WHERE hs.id = :id")
    Optional<String> findServiceNameById(@Param("id") Long id);

    @Query("SELECT hs.veterinaryService.price FROM HeadquarterVetService hs WHERE hs.id = :id")
    Optional<Double> findServicePriceById(Long id);

    @Query("SELECT hs.veterinaryService.specie.specieId FROM HeadquarterVetService hs WHERE hs.id = :id")
    Optional<String> findSpecieNameById(Long id);

    @Query(value = "SELECT e.employee_id AS id, e.name || ' ' || e.last_name AS fullName " +
            "FROM employee e " +
            "JOIN employee_role er ON e.employee_id = er.id_employee " +
            "JOIN role r ON er.id_role = r.role_id " +
            "JOIN headquarter h ON e.id_headquarter = h.headquarter_id " +
            "JOIN headquarter_vet_service hs ON hs.id_headquarter = h.headquarter_id " +
            "WHERE hs.id = :headquarterVetServiceId " +
            "AND e.status = 1 " +
            "AND r.name = 'Veterinario'", nativeQuery = true)
    List<Object[]> findVeterinariansByHeadquarterVetServiceId(
            @Param("headquarterVetServiceId") Long headquarterVetServiceId);

    @Query(value = """
    SELECT 
        h.headquarter_id AS headquarterId,
        h.name AS headquarterName,
        s.service_id AS serviceId,
        s.name AS serviceName,
        s.description AS serviceDescription,
        s.price AS servicePrice,
        s.duration AS serviceDuration,
        c.name AS categoryName,
        sp.name AS speciesName
    FROM headquarter_vet_service hvs
    JOIN headquarter h ON h.headquarter_id = hvs.id_headquarter
    JOIN veterinary_service s ON s.service_id = hvs.id_service
    JOIN category c ON c.category_id = s.id_category
    JOIN specie sp ON sp.specie_id = s.id_specie
    WHERE (:serviceName IS NULL OR LOWER(s.name) LIKE LOWER('%' || :serviceName || '%'))
      AND (:categoryId IS NULL OR c.category_id = :categoryId)
      AND (:speciesId IS NULL OR sp.specie_id = :speciesId)
      AND (:headquarterId IS NULL OR h.headquarter_id = :headquarterId)
      AND hvs.status = 1
    """, nativeQuery = true)
    List<Object[]> findFilteredServices(
            @Param("serviceName") String serviceName,
            @Param("categoryId") Long categoryId,
            @Param("speciesId") Long speciesId,
            @Param("headquarterId") Long headquarterId
    );



}
