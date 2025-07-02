package com.veterinaria.veterinaria_comoreyes.repository;

import java.util.List;
import java.util.Optional;

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


    @Query("SELECT hs.veterinaryService.name FROM HeadquarterVetService hs WHERE hs.id = :id")
    Optional<String> findServiceNameById(@Param("id") Long id);

    @Query("SELECT hs.veterinaryService.price FROM HeadquarterVetService hs WHERE hs.id = :id")
    Optional<Double>  findServicePriceById(Long id);

    @Query("SELECT hs.veterinaryService.specie.specieId FROM HeadquarterVetService hs WHERE hs.id = :id")
    Optional<String>  findSpecieNameById(Long id);

    @Query(value = "SELECT e.employee_id AS id, e.name || ' ' || e.last_name AS fullName " +
            "FROM employee e " +
            "JOIN employee_role er ON e.employee_id = er.id_employee " +
            "JOIN role r ON er.id_role = r.role_id " +
            "JOIN headquarter h ON e.id_headquarter = h.headquarter_id " +
            "JOIN headquarter_vet_service hs ON hs.id_headquarter = h.headquarter_id " +
            "WHERE hs.id = :headquarterVetServiceId " +
            "AND e.status = 1 " +
            "AND r.name = 'Veterinario'", nativeQuery = true)
    List<Object[]> findVeterinariansByHeadquarterVetServiceId(@Param("headquarterVetServiceId") Long headquarterVetServiceId);

}
