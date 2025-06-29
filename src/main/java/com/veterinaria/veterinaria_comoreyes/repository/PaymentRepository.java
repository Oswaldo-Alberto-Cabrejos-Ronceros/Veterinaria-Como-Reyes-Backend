package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
