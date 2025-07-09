package com.veterinaria.veterinaria_comoreyes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Care;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CareRepository extends JpaRepository<Care, Long> {

    List<Care> findByAppointment_AppointmentId(Long appointmentId);


    @Query(value = """
    SELECT
        c.care_id,
        TO_CHAR(c.care_date_time, 'DD/MM/YYYY HH24:MI'),
        c.status_care,
        a.name,
        s.name,
        b.name,
        e.name || ' ' || e.last_name,
        vs.name,
        vs.price,
        h.name,
        c.appointment_id
    FROM care c
    JOIN animal a ON a.animal_id = c.animal_id
    JOIN breed b ON a.breed_id = b.breed_id
    JOIN specie s ON b.id_specie = s.specie_id
    LEFT JOIN employee e ON e.employee_id = c.employee_id
    JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    JOIN headquarter h ON h.headquarter_id = hvs.id_headquarter
    WHERE (:fecha IS NULL OR TRUNC(c.care_date_time) = TO_DATE(:fecha, 'YYYY-MM-DD'))
      AND (:idHeadquarter IS NULL OR hvs.id_headquarter = :idHeadquarter)
      AND (:idService IS NULL OR hvs.id_service = :idService)
      AND (:estado IS NULL OR c.status_care = :estado)
    ORDER BY c.care_date_time DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM care c
    JOIN animal a ON a.animal_id = c.animal_id
    JOIN breed b ON a.breed_id = b.breed_id
    JOIN specie s ON b.id_specie = s.specie_id
    LEFT JOIN employee e ON e.employee_id = c.employee_id
    JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    JOIN headquarter h ON h.headquarter_id = hvs.id_headquarter
    WHERE (:fecha IS NULL OR TRUNC(c.care_date_time) = TO_DATE(:fecha, 'YYYY-MM-DD'))
      AND (:idHeadquarter IS NULL OR hvs.id_headquarter = :idHeadquarter)
      AND (:idService IS NULL OR hvs.id_service = :idService)
      AND (:estado IS NULL OR c.status_care = :estado)
    """,
            nativeQuery = true)
    Page<Object[]> searchCaresNative(
            @Param("fecha") String fecha,
            @Param("idHeadquarter") Long idHeadquarter,
            @Param("idService") Long idService,
            @Param("estado") String estado,
            Pageable pageable
    );

    @Query(value = """
    SELECT
        c.care_id AS id,
        'ATENCIÓN' AS type,
        an.name AS animal_name,
        vs.name AS service_name,
        cl.name || ' ' || cl.last_name AS client_name,
        TO_CHAR(c.care_date_time, 'YYYY-MM-DD') AS fecha,
        TO_CHAR(c.care_date_time, 'HH24:MI') AS hora,
        c.status_care AS status
    FROM care c
    JOIN animal an ON an.animal_id = c.animal_id
    JOIN client cl ON cl.client_id = an.client_id
    JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    WHERE c.status_care IN ('EN_CURSO', 'EN_ESPERA')
      AND c.employee_id = :employeeId
    ORDER BY fecha ASC, hora ASC
""", nativeQuery = true)
    List<Object[]> findCaresByEmployeeId(@Param("employeeId") Long employeeId);

    @Query(value = """
        SELECT 
            COUNT(*) AS totalCares,
            COUNT(CASE 
                WHEN TRUNC(c.care_date_time) = TO_DATE(:todayDate, 'YYYY/MM/DD') 
                THEN 1 
            END) AS todayCares
        FROM care c
        WHERE c.status_care = 'COMPLETADO'
    """, nativeQuery = true)
    List<Object[]> getCareStatsToday(@Param("todayDate") String todayDate);


}
