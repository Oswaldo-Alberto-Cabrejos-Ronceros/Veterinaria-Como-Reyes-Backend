package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {
   Optional<Headquarter> findByHeadquarterIdAndStatusIsTrue(Long id);

   boolean existsByHeadquarterIdAndStatusIsTrue(long headquarterId);

   Headquarter findByHeadquarterId(long headquarterId);

   List<Headquarter> findAllByStatusTrue();

   @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterListDTO(" +
         "h.headquarterId, h.name, h.address, h.district, " +
         "CASE WHEN h.status = true THEN 'Activo' ELSE 'Inactivo' END) " +
         "FROM Headquarter h WHERE " +
         "(:name IS NULL OR h.name LIKE %:name%) AND " +
         "(:address IS NULL OR h.address LIKE %:address%) AND " +
         "(:district IS NULL OR h.district LIKE %:district%) AND " +
         "(:status IS NULL OR h.status = :status)")
   Page<HeadquarterListDTO> searchHeadquarters(
         @Param("name") String name,
         @Param("address") String address,
         @Param("district") String district,
         @Param("status") Boolean status,
         Pageable pageable);
}
