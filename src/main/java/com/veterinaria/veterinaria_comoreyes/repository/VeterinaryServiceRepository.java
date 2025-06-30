package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Category;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VeterinaryServiceRepository extends JpaRepository<VeterinaryService,Long> {
List<VeterinaryService> findAllBySpecie(Specie specie);

List<VeterinaryService> findAllByCategory(Category category);

Optional<VeterinaryService> findByServiceIdAndStatusIsTrue (Long id);

    @Modifying
    @Query("UPDATE VeterinaryService v SET v.status = true WHERE v.serviceId = :serviceId")
    void activateVeterinaryService(@Param("serviceId") Long serviceId);
}
