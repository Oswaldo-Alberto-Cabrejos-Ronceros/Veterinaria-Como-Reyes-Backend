package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.StatusCare;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {
  Optional<Headquarter> findByHeadquarterIdAndStatusIsTrue(Long id);

  boolean existsByHeadquarterIdAndStatusIsTrue(long headquarterId);

  Headquarter findByHeadquarterId(long headquarterId);

  List<Headquarter> findAllByStatusTrue();

  @Modifying
  @Query("UPDATE Headquarter h SET h.status = true WHERE h.headquarterId = :headquarterId")
  void activateHeadquarter(@Param("headquarterId") Long headquarterId);

  @Query("""
      SELECT new com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterListDTO(
          h.headquarterId,
          h.name,
          h.phone,
          h.address,
          h.email,
          h.district,
          h.province,
          CASE WHEN h.status = true THEN 'Activo' ELSE 'Inactivo' END
      )
      FROM Headquarter h
      WHERE (:name IS NULL OR h.name LIKE %:name%) AND
            (:phone IS NULL OR h.phone LIKE %:phone%) AND
            (:address IS NULL OR h.address LIKE %:address%) AND
            (:email IS NULL OR h.email LIKE %:email%) AND
            (:district IS NULL OR h.district LIKE %:district%) AND
            (:province IS NULL OR h.province LIKE %:province%) AND
            (:status IS NULL OR h.status = :status)
      """)
  Page<HeadquarterListDTO> searchHeadquartersWithFilters(
      @Param("name") String name,
      @Param("phone") String phone,
      @Param("address") String address,
      @Param("email") String email,
      @Param("district") String district,
      @Param("province") String province,
      @Param("status") Boolean status,
      Pageable pageable);
}
