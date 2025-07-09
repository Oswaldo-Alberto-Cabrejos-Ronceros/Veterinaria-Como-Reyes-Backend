package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            WHERE c.animal_id = :animalId
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
            WHERE c.id_animal = :animalId
        """,
            nativeQuery = true)
    Page<Object[]> findAllInfoVeterinaryRecordsByAnimalId(@Param("animalId") Long animalId, Pageable pageable);


    @Query(value = """
    SELECT 
        vr.id AS veterinary_record_id,
        vr.id_care AS care_id,
        an.name AS animal_name,
        br.name AS breed_name,
        INITCAP(REGEXP_SUBSTR(cl.name, '^\\S+')) || ' ' || INITCAP(REGEXP_SUBSTR(cl.last_name, '^\\S+')) AS client_full_name,
        vs.name AS service_name,
        TO_CHAR(vr.date_created, 'DD/MM/YYYY') AS record_date,
        vr.diagnosis,
        vr.treatment,
        vr.observations,
        vr.status_veterinary_record
    FROM veterinary_record vr
    JOIN care c ON c.care_id = vr.id_care
    JOIN animal an ON an.animal_id = c.animal_id
    JOIN breed br ON br.breed_id = an.breed_id
    JOIN client cl ON cl.client_id = an.client_id
    JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    WHERE vr.id_employee = :employeeId
    ORDER BY vr.date_created DESC
    FETCH FIRST 10 ROWS ONLY
    """, nativeQuery = true)
    List<Object[]> findRecentMedicalRecordsByEmployee(@Param("employeeId") Long employeeId);

    @Query(value = """
    SELECT
        COUNT(*) AS total,
        COUNT(CASE WHEN vr.status_veterinary_record = 'EN_CURSO' THEN 1 END),
        COUNT(CASE WHEN vr.status_veterinary_record = 'COMPLETADO' THEN 1 END),
        COUNT(CASE WHEN vr.status_veterinary_record = 'OBSERVACION' THEN 1 END)
    FROM veterinary_record vr
    WHERE vr.id_employee = :employeeId
""", nativeQuery = true)
    List<Object[]> getVeterinaryRecordStatsByEmployee(@Param("employeeId") Long employeeId);


    @Query(value = """
    SELECT 
        vr.id,
        TO_CHAR(vr.date_created, 'DD/MM/YYYY') AS formatted_date,
        h.name AS headquarter_name,
        e.name || ' ' || e.last_name AS employee_full_name,
        vr.diagnosis,
        vr.treatment,
        vr.observations,
        vr.result_url,
        vr.status_veterinary_record
    FROM veterinary_record vr
    JOIN care c ON c.care_id = vr.id_care
    JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
    JOIN headquarter h ON h.headquarter_id = hvs.id_headquarter
    JOIN employee e ON e.employee_id = vr.id_employee
    WHERE c.animal_id = :animalId
    ORDER BY vr.date_created DESC
""", nativeQuery = true)
    List<Object[]> findRecordsByAnimalId(@Param("animalId") Long animalId);


}