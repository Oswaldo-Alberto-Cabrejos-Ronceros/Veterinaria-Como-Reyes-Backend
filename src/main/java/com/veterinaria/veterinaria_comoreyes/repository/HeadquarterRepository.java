package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
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

}
