package com.veterinaria.veterinaria_comoreyes.repository;

import java.util.List;
import java.util.Optional;

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
      ORDER BY c.care_id DESC
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
            an.animal_id AS animalId,
            an.name AS animal_name,
            vs.name AS service_name,
            cl.name || ' ' || cl.last_name AS client_name,
            TO_CHAR(c.care_date_time, 'YYYY-MM-DD') AS fecha,
            TO_CHAR(c.care_date_time, 'HH24:MI') AS hora,
            c.status_care AS status,
            '' AS commentAppointment,
            c.employee_id AS employeeId,
            e.name || ' ' || e.last_name AS employeeName,
            br.name AS breedName
        FROM care c
        JOIN animal an ON an.animal_id = c.animal_id
        JOIN client cl ON cl.client_id = an.client_id
        JOIN breed br ON an.breed_id = br.breed_id
        JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
        JOIN veterinary_service vs ON vs.service_id = hvs.id_service
        JOIN employee e ON e.employee_id = c.employee_id
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
    JOIN headquarter_vet_service hvs ON c.headquarter_vetservice_id = hvs.id
    WHERE c.status_care = 'COMPLETADO'
      AND hvs.id_headquarter = :headquarterId
    """, nativeQuery = true)
    List<Object[]> getCareStatsToday(
            @Param("todayDate") String todayDate,
            @Param("headquarterId") Long headquarterId
    );


    @Query(value = """
    SELECT
        c.care_id AS id,
        'ATENCIÓN' AS type,
        an.animal_id AS animalId,
        an.name AS animal_name,
        vs.name AS service_name,
        cl.name || ' ' || cl.last_name AS client_name,
        TO_CHAR(c.care_date_time, 'YYYY-MM-DD') AS fecha,
        TO_CHAR(c.care_date_time, 'HH24:MI') AS hora,
        c.status_care AS status,
        '' AS commentAppointment,
        c.employee_id AS employeeId,
        e.name || ' ' || e.last_name AS employeeName,
        br.name AS breedName
    FROM care c
    JOIN animal an ON an.animal_id = c.animal_id
    JOIN client cl ON cl.client_id = an.client_id
    JOIN breed br ON an.breed_id = br.breed_id
    JOIN headquarter_vet_service hvs ON hvs.id = c.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    JOIN employee e ON e.employee_id = c.employee_id
    WHERE c.status_care IN ('EN_CURSO', 'EN_ESPERA')
      AND hvs.id_headquarter = :headquarterId
    ORDER BY fecha ASC, hora ASC
""", nativeQuery = true)
    List<Object[]> findCaresByHeadquarterId(@Param("headquarterId") Long headquarterId);

    @Query(value = """
    SELECT 
        a.animal_id,
        a.name AS animal_name,
        b.name AS breed_name,
        INITCAP(REGEXP_SUBSTR(c.name, '^\\S+')) || ' ' || INITCAP(REGEXP_SUBSTR(c.last_name, '^\\S+')) AS client_full_name,
        TO_CHAR(MAX(cr.care_date_time), 'DD/MM/YYYY') AS last_visit_date,
        a.weight AS animal_weight,
        CASE 
            WHEN a.gender = 'M' THEN 'Macho'
            WHEN a.gender = 'H' THEN 'Hembra'
            ELSE 'Unknown'
        END AS animal_sex,
        TO_CHAR(a.birth_date, 'DD/MM/YYYY') AS animal_birth_date
    FROM care cr
    JOIN animal a ON cr.animal_id = a.animal_id
    JOIN breed b ON a.breed_id = b.breed_id
    JOIN client c ON a.client_id = c.client_id
    WHERE cr.employee_id = :employeeId
      AND cr.status_care = 'COMPLETADO'
    GROUP BY a.animal_id, a.name, b.name, c.name, c.last_name, a.weight, a.gender, a.birth_date
    ORDER BY MAX(cr.care_date_time) DESC
    FETCH FIRST 10 ROWS ONLY
    """, nativeQuery = true)
    List<Object[]> findRecentPatientsByEmployee(@Param("employeeId") Long employeeId);

    @Query(value = """
    SELECT COUNT(DISTINCT c.animal_id)
    FROM care c
    WHERE c.employee_id = :employeeId
      AND c.status_care = 'COMPLETADO'
      AND c.care_date_time >= ADD_MONTHS(TRUNC(SYSDATE), -1)
""", nativeQuery = true)
    Long countDistinctAnimalsLastMonth(@Param("employeeId") Long employeeId);

    @Query(value = """
    SELECT COUNT(*)
    FROM care c
    WHERE c.employee_id = :employeeId
      AND c.status_care = 'COMPLETADO'
      AND c.care_date_time >= ADD_MONTHS(TRUNC(SYSDATE), -1)
""", nativeQuery = true)
    Long countCompletedCaresLastMonth(@Param("employeeId") Long employeeId);

    @Query(
            value = """
            SELECT
            TO_CHAR(s.fecha, 'DD/MM') AS fecha,
            COALESCE(r.total, 0) AS total_cares
        FROM (
            SELECT TRUNC(TO_DATE(:fechaBase, 'DD/MM/YYYY')) - (LEVEL - 1) * 7 AS fecha
            FROM dual
            CONNECT BY LEVEL <= 5
        ) s
        LEFT JOIN (
            SELECT
                TRUNC(c.care_date_time) AS fecha,
                COUNT(*) AS total
            FROM CARE c
            WHERE c.status_care = 'COMPLETADO'
              AND c.employee_id = :employeeId
            GROUP BY TRUNC(c.care_date_time)
        ) r
        ON s.fecha = r.fecha
        ORDER BY s.fecha ASC
        
        """,
            nativeQuery = true
    )
    List<Object[]> getWeeklyCareStats(@Param("fechaBase") String fechaBase, @Param("employeeId") Long employeeId);


    @Query(value = """
    SELECT 
        -- Total de pacientes únicos atendidos este mes
        (SELECT COUNT(DISTINCT c.ANIMAL_ID)
         FROM CARE c
         WHERE c.STATUS_CARE = 'COMPLETADO'
           AND c.CARE_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND c.CARE_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
        ),

        -- Total de clientes únicos este mes
        (SELECT COUNT(DISTINCT cl.CLIENT_ID)
         FROM CARE c
         JOIN ANIMAL a ON c.ANIMAL_ID = a.ANIMAL_ID
         JOIN CLIENT cl ON a.CLIENT_ID = cl.CLIENT_ID
         WHERE c.STATUS_CARE = 'COMPLETADO'
           AND c.CARE_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND c.CARE_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
        ),

        -- Veterinarios activos este mes
        (SELECT COUNT(DISTINCT c.EMPLOYEE_ID)
         FROM CARE c
         WHERE c.STATUS_CARE = 'COMPLETADO'
           AND c.CARE_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND c.CARE_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
        ),

        -- Promedio de ingresos por veterinario
        (SELECT CASE 
                    WHEN COUNT(DISTINCT c.EMPLOYEE_ID) = 0 THEN 0
                    ELSE ROUND(SUM(p.AMOUNT) / COUNT(DISTINCT c.EMPLOYEE_ID), 2)
                END
         FROM PAYMENT p
         JOIN CARE c ON p.CARE_ID = c.CARE_ID
         WHERE p.STATUS = 'COMPLETADA'
           AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND p.PAYMENT_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
        ) 
    FROM DUAL
""", nativeQuery = true)
    List<Object[]> getGeneralOperationalMonthlyStats();

    @Query(value = """
    SELECT 
        -- Total de pacientes únicos atendidos este mes
        (SELECT COUNT(DISTINCT c.ANIMAL_ID)
         FROM CARE c
         JOIN HEADQUARTER_VET_SERVICE hv ON c.HEADQUARTER_VETSERVICE_ID = hv.ID
         WHERE c.STATUS_CARE = 'COMPLETADO'
           AND c.CARE_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND c.CARE_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
           AND hv.ID_HEADQUARTER = :headquarterId
        ),

        -- Total de clientes únicos este mes
        (SELECT COUNT(DISTINCT cl.CLIENT_ID)
         FROM CARE c
         JOIN HEADQUARTER_VET_SERVICE hv ON c.HEADQUARTER_VETSERVICE_ID = hv.ID
         JOIN ANIMAL a ON c.ANIMAL_ID = a.ANIMAL_ID
         JOIN CLIENT cl ON a.CLIENT_ID = cl.CLIENT_ID
         WHERE c.STATUS_CARE = 'COMPLETADO'
           AND c.CARE_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND c.CARE_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
           AND hv.ID_HEADQUARTER = :headquarterId
        ) ,

        -- Veterinarios activos este mes
        (SELECT COUNT(DISTINCT c.EMPLOYEE_ID)
         FROM CARE c
         JOIN HEADQUARTER_VET_SERVICE hv ON c.HEADQUARTER_VETSERVICE_ID = hv.ID
         WHERE c.STATUS_CARE = 'COMPLETADO'
           AND c.CARE_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND c.CARE_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
           AND hv.ID_HEADQUARTER = :headquarterId
        ),

        -- Promedio de ingresos por veterinario
        (SELECT CASE 
                    WHEN COUNT(DISTINCT c.EMPLOYEE_ID) = 0 THEN 0
                    ELSE ROUND(SUM(p.AMOUNT) / COUNT(DISTINCT c.EMPLOYEE_ID), 2)
                END
         FROM PAYMENT p
         JOIN CARE c ON p.CARE_ID = c.CARE_ID
         JOIN HEADQUARTER_VET_SERVICE hv ON c.HEADQUARTER_VETSERVICE_ID = hv.ID
         WHERE p.STATUS = 'COMPLETADA'
           AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'MM')
           AND p.PAYMENT_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)
           AND hv.ID_HEADQUARTER = :headquarterId
        ) 
    FROM DUAL
""", nativeQuery = true)
    List<Object[]> getOperationalMonthlyStatsByHeadquarter(@Param("headquarterId") Long headquarterId);

    @Query(value = """
    SELECT
        COUNT(DISTINCT c.CARE_ID) AS total_cares,
        COUNT(DISTINCT c.ANIMAL_ID) AS total_patients,
        COUNT(DISTINCT vr.ID) AS total_records
    FROM EMPLOYEE e
    LEFT JOIN CARE c ON e.EMPLOYEE_ID = c.EMPLOYEE_ID
        AND c.STATUS_CARE = 'COMPLETADA'
        AND TRUNC(c.CARE_DATE_TIME) = TRUNC(SYSDATE)
    LEFT JOIN VETERINARY_RECORD vr ON c.CARE_ID = vr.ID_CARE
        AND TRUNC(vr.DATE_CREATED) = TRUNC(SYSDATE)
    WHERE e.EMPLOYEE_ID = :veterinarianId
""", nativeQuery = true)
    List<Object[]> getStatsForVeterinarianPanelToday(@Param("veterinarianId") Long veterinarianId);


}
