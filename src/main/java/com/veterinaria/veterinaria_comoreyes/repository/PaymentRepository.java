package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByAppointment_AppointmentIdAndStatus(Long appointmentId, String status);

    Optional<Payment> findByCare_CareIdAndStatus(Long careId, String status);

    @Query(value = """
            SELECT 
              p.payment_id AS id,
              h.name AS headquarterName,
              s.name AS serviceName,
              cclient.dni AS clientDni,
              p.amount AS amount,
              p.status AS status,
              pm.name AS paymentMethod,
              TO_CHAR(p.payment_date_time, 'YYYY-MM-DD') AS paymentDate
            FROM payment p
            LEFT JOIN appointment a ON p.appointment_id = a.appointment_id
            LEFT JOIN animal an ON a.animal_id = an.animal_id
            LEFT JOIN client cclient ON an.client_id = cclient.client_id
            LEFT JOIN headquarter_vet_service hs ON a.headquarter_vetservice_id = hs.id
            LEFT JOIN headquarter h ON hs.id_headquarter = h.headquarter_id
            LEFT JOIN veterinary_service s ON hs.id_service = s.service_id
            LEFT JOIN payment_method pm ON p.payment_method_id = pm.payment_method_id
            ORDER BY p.creation_date DESC
            """, nativeQuery = true)
    Page<Object[]> findAllPaymentsForTable(Pageable pageable);

    @Query(value = """
        SELECT
          p.payment_id   AS id,
          h.name         AS headquarterName,
          s.name         AS serviceName,
          c.dni          AS clientDni,
          p.amount       AS amount,
          p.status       AS status,
          pm.name        AS paymentMethod,
          TO_CHAR(p.payment_date_time, 'YYYY-MM-DD') AS paymentDate
        FROM payment p
        LEFT JOIN appointment a     ON p.appointment_id = a.appointment_id
        LEFT JOIN animal an         ON a.animal_id = an.animal_id
        LEFT JOIN client c          ON an.client_id = c.client_id
        LEFT JOIN headquarter_vet_service hs ON a.headquarter_vetservice_id = hs.id
        LEFT JOIN headquarter h     ON hs.id_headquarter = h.headquarter_id
        LEFT JOIN veterinary_service s ON hs.id_service = s.service_id
        LEFT JOIN payment_method pm ON p.payment_method_id = pm.payment_method_id
        WHERE (:dni IS NULL OR c.dni = :dni)
          AND (:headquarterId IS NULL OR h.headquarter_id = :headquarterId)
          AND (:serviceId IS NULL OR s.service_id = :serviceId)
          AND (:status IS NULL OR p.status = :status)
          AND (:startDate IS NULL OR p.payment_date_time >= TO_DATE(:startDate, 'YYYY-MM-DD'))
          AND (:endDate IS NULL OR p.payment_date_time <= TO_DATE(:endDate, 'YYYY-MM-DD') + INTERVAL '23:59:59' HOUR TO SECOND)
        ORDER BY p.payment_date_time DESC      
       """,
            nativeQuery = true)
    Page<Object[]> searchPaymentsForTable(
            @Param("dni") String dni,
            @Param("headquarterId") Long headquarterId,
            @Param("serviceId") Long serviceId,
            @Param("status") String status,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Payment p SET p.status = :status WHERE p.paymentId = :paymentId")
    void updateStatus(@Param("paymentId") Long paymentId, @Param("status") PaymentStatus status);

    @Modifying
    @Query(value = "UPDATE payment SET care_id = :careId WHERE payment_id = :paymentId", nativeQuery = true)
    void updateCareIdByPaymentId(@Param("paymentId") Long paymentId, @Param("careId") Long careId);

    @Query("SELECT p.paymentId FROM Payment p WHERE p.appointment.appointmentId = :appointmentId")
    Long findPaymentIdByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query(value = """
    SELECT
        NVL(SUM(CASE 
            WHEN EXTRACT(MONTH FROM payment_date_time) = :currentMonth
             AND EXTRACT(YEAR FROM payment_date_time) = :currentYear
             AND status = 'COMPLETADA' THEN amount
            ELSE 0 
        END), 0) AS current_total,

        NVL(SUM(CASE 
            WHEN EXTRACT(MONTH FROM payment_date_time) = :previousMonth
             AND EXTRACT(YEAR FROM payment_date_time) = :previousYear
             AND status = 'COMPLETADA' THEN amount 
            ELSE 0 
        END), 0) AS previous_total
    FROM payment
""", nativeQuery = true)
    Object getCompletedPaymentStats(
            @Param("currentMonth") int currentMonth,
            @Param("currentYear") int currentYear,
            @Param("previousMonth") int previousMonth,
            @Param("previousYear") int previousYear
    );

    @Query(value = """
    SELECT
        NVL(SUM(CASE 
            WHEN EXTRACT(MONTH FROM p.payment_date_time) = :currentMonth
             AND EXTRACT(YEAR FROM p.payment_date_time) = :currentYear
             AND p.status = 'COMPLETADA'
             AND (
                (p.care_id IS NOT NULL AND c.headquarter_vetservice_id IN (
                    SELECT hvs.id FROM headquarter_vet_service hvs WHERE hvs.id_headquarter = :headquarterId
                ))
                OR
                (p.care_id IS NULL AND a.headquarter_vetservice_id IN (
                    SELECT hvs.id FROM headquarter_vet_service hvs WHERE hvs.id_headquarter = :headquarterId
                ))
             )
            THEN p.amount ELSE 0 
        END), 0) AS current_total,

        NVL(SUM(CASE 
            WHEN EXTRACT(MONTH FROM p.payment_date_time) = :previousMonth
             AND EXTRACT(YEAR FROM p.payment_date_time) = :previousYear
             AND p.status = 'COMPLETADA'
             AND (
                (p.care_id IS NOT NULL AND c.headquarter_vetservice_id IN (
                    SELECT hvs.id FROM headquarter_vet_service hvs WHERE hvs.id_headquarter = :headquarterId
                ))
                OR
                (p.care_id IS NULL AND a.headquarter_vetservice_id IN (
                    SELECT hvs.id FROM headquarter_vet_service hvs WHERE hvs.id_headquarter = :headquarterId
                ))
             )
            THEN p.amount ELSE 0 
        END), 0) AS previous_total
    FROM payment p
    LEFT JOIN care c ON p.care_id = c.care_id
    LEFT JOIN appointment a ON p.appointment_id = a.appointment_id
""", nativeQuery = true)
    Object getPaymentsStatsByHeadquarter(
            @Param("currentMonth") int currentMonth,
            @Param("currentYear") int currentYear,
            @Param("previousMonth") int previousMonth,
            @Param("previousYear") int previousYear,
            @Param("headquarterId") Long headquarterId
    );

    @Query(value = """
    SELECT 
        COALESCE(SUM(p.amount), 0)
    FROM payment p
    WHERE p.status = 'COMPLETADA'
      AND TRUNC(p.payment_date_time) = TO_DATE(:today, 'YYYY/MM/DD')
""", nativeQuery = true)
    BigDecimal getTodayIncome(@Param("today") String today);

    @Query(value = """
    SELECT
        p.payment_id,
        INITCAP(REGEXP_SUBSTR(cl.name, '^\\S+')) || ' ' || INITCAP(REGEXP_SUBSTR(cl.last_name, '^\\S+')) AS client_full_name,
        UPPER(SUBSTR(cl.name, 1, 1) || SUBSTR(cl.last_name, 1, 1)) AS client_initials,
        an.name AS animal_name,
        vs.name AS service_name,
        p.amount,
        CASE 
            WHEN p.status = 'COMPLETADA' THEN TO_CHAR(p.payment_date_time, 'DD/MM/YYYY') 
            ELSE NULL 
        END AS payment_date,
        CASE 
            WHEN p.status = 'COMPLETADA' THEN TO_CHAR(p.payment_date_time, 'HH24:MI') 
            ELSE NULL 
        END AS payment_time,
        p.status
    FROM payment p
    LEFT JOIN care c ON c.care_id = p.care_id
    LEFT JOIN appointment a ON a.appointment_id = p.appointment_id AND p.care_id IS NULL
    LEFT JOIN animal an ON an.animal_id = COALESCE(c.animal_id, a.animal_id)
    LEFT JOIN client cl ON cl.client_id = an.client_id
    LEFT JOIN headquarter_vet_service hvs ON hvs.id = COALESCE(c.headquarter_vetservice_id, a.headquarter_vetservice_id)
    LEFT JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    WHERE p.status = 'COMPLETADA'
      AND hvs.id_headquarter = :headquarterId
    ORDER BY p.payment_date_time DESC
    FETCH FIRST 10 ROWS ONLY
""", nativeQuery = true)
    List<Object[]> findRecentCompletedPaymentsByHeadquarter(@Param("headquarterId") Long headquarterId);

    @Query(value = """
    WITH week_days AS (
        SELECT TO_DATE(:startDate, 'YYYY-MM-DD') + LEVEL - 1 AS day_date
        FROM dual
        CONNECT BY LEVEL <= 7
    )
    SELECT 
        INITCAP(TO_CHAR(wd.day_date, 'Day', 'NLS_DATE_LANGUAGE=SPANISH')) AS day_name,
        NVL(SUM(p.amount), 0) AS total
    FROM week_days wd
    LEFT JOIN payment p 
        ON TRUNC(p.payment_date_time) = wd.day_date 
        AND p.status = 'COMPLETADA'
    GROUP BY wd.day_date
    ORDER BY wd.day_date ASC
    """, nativeQuery = true)
    List<Object[]> getWeeklyIncomeGeneral(@Param("startDate") String startDate);


    @Query(value = """

            WITH week_days AS (
            SELECT TO_DATE(:startDate, 'YYYY/MM/DD') + LEVEL - 1 AS day_date
        FROM dual
            CONNECT BY LEVEL <= 7
    )
    SELECT\s
        INITCAP(
            TO_CHAR(wd.day_date, 'Day', 'NLS_DATE_LANGUAGE=SPANISH')) AS day_name,
        NVL
            (SUM(CASE\s
            WHEN hvs.id_headquarter = :headquarterId THEN p.amount
            ELSE 0
        END), 0) AS total
            FROM week_days wd
            LEFT JOIN payment p\s
        ON TRUNC(p.payment_date_time) = wd.day_date AND p.status = 'COMPLETADA'
            LEFT JOIN care c ON c.care_id = p.care_id
            LEFT JOIN appointment a ON a.appointment_id = p.appointment_id AND p.care_id IS NULL
            LEFT JOIN headquarter_vet_service hvs\s
        ON hvs.id = COALESCE(c.headquarter_vetservice_id, a.headquarter_vetservice_id)
            GROUP BY wd.day_date
            ORDER BY wd.day_date ASC
    
    """, nativeQuery = true)
    List<Object[]> getWeeklyIncomeByHeadquarter(
            @Param("startDate") String startDate,
            @Param("headquarterId") Long headquarterId
    );



}
