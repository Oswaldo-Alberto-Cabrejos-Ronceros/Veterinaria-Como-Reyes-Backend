package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpecieRepository extends JpaRepository<Specie, Long> {

  Optional<Specie> findBySpecieIdAndStatusIsTrue(Long id);

  @Modifying
  @Query("UPDATE Specie s SET s.status = true WHERE s.specieId = :specieId")
  void activateSpecie(@Param("specieId") Long specieId);

  @Query("""
          SELECT new com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO(
              s.specieId,
              s.name,
              CASE WHEN s.status = true THEN 'Activo' ELSE 'Inactivo' END
          )
          FROM Specie s
          WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT(:name, '%')))
            AND (:status IS NULL OR s.status = :status)
          ORDER BY s.specieId DESC
      """)
  Page<SpecieListDTO> searchSpeciesWithFilters(
      @Param("name") String name,
      @Param("status") Boolean status,
      Pageable pageable);

  @Modifying
  @Query(value = "UPDATE breed SET status = 0 WHERE id_specie = :specieId", nativeQuery = true)
  void disableBreedsBySpecieId(@Param("specieId") Long specieId);

  @Query(value = """
      SELECT sp.name AS species_name, COUNT(*) AS total_completed
      FROM appointment a
      JOIN animal an ON a.animal_id = an.animal_id
      JOIN breed br ON an.breed_id = br.breed_id
      JOIN specie sp ON br.id_specie = sp.specie_id
      WHERE a.status_appointments = 'COMPLETADA'
      GROUP BY sp.name
      ORDER BY total_completed DESC
      FETCH FIRST 4 ROWS ONLY
      """, nativeQuery = true)
  List<Object[]> findTopSpeciesWithMostCompletedAppointments();

  @Query(value = """
      SELECT sp.name AS species_name, COUNT(*) AS total_completed
      FROM appointment a
      JOIN animal an ON a.animal_id = an.animal_id
      JOIN breed br ON an.breed_id = br.breed_id
      JOIN specie sp ON br.id_specie = sp.specie_id
      JOIN headquarter_vet_service hvs ON hvs.id = a.headquarter_vetservice_id
      WHERE a.status_appointments = 'COMPLETADA'
        AND hvs.id_headquarter = :headquarterId
      GROUP BY sp.name
      ORDER BY total_completed DESC
      FETCH FIRST 4 ROWS ONLY
      """, nativeQuery = true)
  List<Object[]> findTopSpeciesWithMostCompletedAppointmentsByHeadquarter(@Param("headquarterId") Long headquarterId);





  @Query(value = """
    WITH especies_filtradas AS (
        SELECT\s
            s.NAME AS especie,
            COUNT(*) AS total
        FROM PAYMENT p
        LEFT JOIN APPOINTMENT a ON p.APPOINTMENT_ID = a.APPOINTMENT_ID
        LEFT JOIN CARE c ON p.CARE_ID = c.CARE_ID
        LEFT JOIN ANIMAL an ON an.ANIMAL_ID = COALESCE(a.ANIMAL_ID, c.ANIMAL_ID)
        LEFT JOIN BREED b ON an.BREED_ID = b.BREED_ID
        LEFT JOIN SPECIE s ON b.ID_SPECIE = s.SPECIE_ID
        WHERE p.STATUS = 'COMPLETADA'
          AND p.PAYMENT_DATE_TIME IS NOT NULL
          AND (
            (:period = 'WEEK' AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'IW') AND p.PAYMENT_DATE_TIME < TRUNC(SYSDATE, 'IW') + 7) OR
            (:period = 'MONTH' AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'MM') AND p.PAYMENT_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)) OR
            (:period = 'YEAR' AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'YYYY') AND p.PAYMENT_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'YYYY'), 12))
          )
        GROUP BY s.NAME
    ),
    ranking AS (
        SELECT especie, total, ROW_NUMBER() OVER (ORDER BY total DESC) AS rn
        FROM especies_filtradas
    ),
    top_4 AS (
        SELECT especie, total FROM ranking WHERE rn <= 4
    ),
    otros_raw AS (
        SELECT SUM(total) AS total FROM ranking WHERE rn > 4
    ),
    otros AS (
        SELECT 'Otros' AS especie, COALESCE((SELECT total FROM otros_raw), 0) AS total FROM DUAL
    ),
    top_con_otros AS (
        SELECT * FROM top_4
        UNION ALL
        SELECT * FROM otros
    ),
    conteo_top AS (
        SELECT COUNT(*) AS cantidad FROM top_4
    ),
    completar AS (
        SELECT s.NAME AS especie, 0 AS total
        FROM SPECIE s, conteo_top
        WHERE s.NAME NOT IN (SELECT especie FROM top_con_otros)
          AND ROWNUM <= (4 - conteo_top.cantidad)
    )
    SELECT especie, total
    FROM (
        SELECT especie, total, 1 AS prioridad FROM top_con_otros
        UNION ALL
        SELECT especie, total, 2 AS prioridad FROM completar
    )
    ORDER BY prioridad, total DESC
""", nativeQuery = true)
  List<Object[]> findTopSpeciesByPeriod(@Param("period") String period);

  @Query(value = """
        WITH especies_filtradas AS (
            SELECT\s
                COALESCE(s.NAME, 'Sin especie') AS especie,
                COUNT(*) AS total
            FROM PAYMENT p
            LEFT JOIN APPOINTMENT a ON p.APPOINTMENT_ID = a.APPOINTMENT_ID
            LEFT JOIN CARE c ON p.CARE_ID = c.CARE_ID
            LEFT JOIN ANIMAL an ON an.ANIMAL_ID = COALESCE(a.ANIMAL_ID, c.ANIMAL_ID)
            LEFT JOIN BREED b ON an.BREED_ID = b.BREED_ID
            LEFT JOIN SPECIE s ON b.ID_SPECIE = s.SPECIE_ID
            LEFT JOIN HEADQUARTER_VET_SERVICE hv ON hv.ID = COALESCE(a.HEADQUARTER_VETSERVICE_ID, c.HEADQUARTER_VETSERVICE_ID)
            WHERE p.STATUS = 'COMPLETADA'
              AND p.PAYMENT_DATE_TIME IS NOT NULL
              AND hv.ID_HEADQUARTER = :headquarterId
              AND (
                (:period = 'WEEK' AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'IW') AND p.PAYMENT_DATE_TIME < TRUNC(SYSDATE, 'IW') + 7) OR
                (:period = 'MONTH' AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'MM') AND p.PAYMENT_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'MM'), 1)) OR
                (:period = 'YEAR' AND p.PAYMENT_DATE_TIME >= TRUNC(SYSDATE, 'YYYY') AND p.PAYMENT_DATE_TIME < ADD_MONTHS(TRUNC(SYSDATE, 'YYYY'), 12))
              )
            GROUP BY s.NAME
        ),
        ranking AS (
            SELECT especie, total, ROW_NUMBER() OVER (ORDER BY total DESC) AS rn FROM especies_filtradas
        ),
        top_4 AS (
            SELECT especie, total FROM ranking WHERE rn <= 4
        ),
        otros AS (
            SELECT 'Otros' AS especie, COALESCE(SUM(total), 0) AS total FROM ranking WHERE rn > 4
        ),
        resultado AS (
            SELECT especie, total FROM top_4
            UNION ALL
            SELECT especie, total FROM otros
        ),
        completar AS (
            SELECT s.NAME AS especie, 0 AS total
            FROM SPECIE s
            WHERE s.NAME NOT IN (SELECT especie FROM resultado)
              AND ROWNUM <= (4 - (SELECT COUNT(*) FROM resultado WHERE especie != 'Otros'))
        )
        SELECT especie, total
        FROM (
            SELECT especie, total, 1 AS prioridad FROM resultado
            UNION ALL
            SELECT especie, total, 2 AS prioridad FROM completar
        )
        ORDER BY prioridad, total DESC
        
""", nativeQuery = true)
  List<Object[]> findTopSpeciesByPeriodAndHeadquarter(@Param("period") String period, @Param("headquarterId") Long headquarterId);

}
