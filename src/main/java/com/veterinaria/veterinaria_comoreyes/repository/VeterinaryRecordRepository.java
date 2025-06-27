package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinaryRecordRepository extends JpaRepository<VeterinaryRecord, Long> {
}
