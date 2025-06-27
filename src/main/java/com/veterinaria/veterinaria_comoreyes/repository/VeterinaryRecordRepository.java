package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeterinaryRecordRepository extends JpaRepository<VeterinaryRecord, Long> {

    @Query(value = """
        SELECT v.id, v.date_created, h.name AS name_headquarter, e.name AS name_employee, v.diagnosis, v.treatment, v.observations, v.result_url, v.status_veterinary_record
        FROM veterinary_record v
        JOIN care c ON v.id_care = c.care_id
        JOIN headquarter_vet_service hvs ON c.headquarter_vetservice_id = hvs.id
        JOIN headquarter h ON hvs.id_headquarter = h.headquarter_id
        LEFT JOIN employee e ON v.id_employee = e.employee_id
        ORDER BY 
            CASE v.status_veterinary_record 
                WHEN 'EN_CURSO' THEN 1
                WHEN 'OBSERVACION' THEN 2
                WHEN 'COMPLETADO' THEN 3
                ELSE 4
            END,
            v.date_created DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM veterinary_record v
        JOIN care c ON v.id_care = c.care_id
        JOIN headquarter_vet_service hvs ON c.headquarter_vetservice_id = hvs.id
        JOIN headquarter h ON hvs.id_headquarter = h.headquarter_id
        LEFT JOIN employee e ON v.id_employee = e.employee_id
    """,
            nativeQuery = true)
    Page<Object[]> findAllInfoVeterinaryRecords(Pageable pageable);

}