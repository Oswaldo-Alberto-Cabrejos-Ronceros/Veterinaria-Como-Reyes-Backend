package com.veterinaria.veterinaria_comoreyes.external.reports.financial.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.veterinaria.veterinaria_comoreyes.entity.Payment;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByPeriodAndServiceDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByPeriodDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByServiceDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeBySpecieDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.PaymentMethodUsageDTO;

public interface FinancialReportRepository extends JpaRepository<Payment, Long> {

        @Query(value = """
                        SELECT
                            TO_CHAR(TRUNC(p.payment_date_time), :datePattern) as period,
                            NVL(SUM(p.amount), 0) as total
                        FROM payment p
                        WHERE p.status = 'COMPLETADA'
                          AND p.payment_date_time BETWEEN :startDate AND :endDate
                        GROUP BY TRUNC(p.payment_date_time), TO_CHAR(TRUNC(p.payment_date_time), :datePattern)
                        ORDER BY period
                        """, nativeQuery = true)
        List<IncomeByPeriodDTO> findIncomeByPeriod(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        @Param("datePattern") String datePattern);

        @Query(value = """
                        SELECT
                            vs.name AS serviceName,
                            c.name AS category,
                            SUM(p.amount) AS total,
                            COUNT(*) AS count
                        FROM PAYMENT p
                        JOIN APPOINTMENT a ON p.APPOINTMENT_ID = a.APPOINTMENT_ID
                        JOIN HEADQUARTER_VET_SERVICE hvs ON a.HEADQUARTER_VETSERVICE_ID = hvs.ID
                        JOIN VETERINARY_SERVICE vs ON hvs.ID_SERVICE = vs.SERVICE_ID
                        JOIN CATEGORY c ON vs.ID_CATEGORY = c.CATEGORY_ID
                        WHERE p.STATUS = 'COMPLETADA'
                        GROUP BY vs.name, c.name
                        ORDER BY total DESC
                        """, nativeQuery = true)
        List<IncomeByServiceDTO> findIncomeByService();

        @Query(value = """
                        SELECT
                            s.name AS specieName,
                            SUM(p.amount) AS total,
                            COUNT(*) AS count
                        FROM payment p
                        JOIN appointment a ON a.appointment_id = p.appointment_id
                        JOIN animal an ON a.animal_id = an.animal_id
                        JOIN breed b ON an.breed_id = b.breed_id
                        JOIN specie s ON b.ID_SPECIE = s.SPECIE_ID
                        WHERE p.status = 'COMPLETADA'
                        GROUP BY s.name
                        ORDER BY total DESC
                        """, nativeQuery = true)
        List<IncomeBySpecieDTO> findIncomeBySpecie();

        @Query(value = """
                        SELECT
                            pm.name AS methodName,
                            SUM(p.amount) AS total,
                            COUNT(*) AS count
                        FROM payment p
                        JOIN payment_method pm ON p.payment_method_id = pm.payment_method_id
                        WHERE p.status = 'COMPLETADA'
                        GROUP BY pm.name
                        ORDER BY total DESC
                        """, nativeQuery = true)
        List<PaymentMethodUsageDTO> findIncomeByPaymentMethod();

        @Query(value = """
                            SELECT
                                TO_CHAR(TRUNC(p.payment_date_time, 'YYYY'), 'YYYY') AS period,
                                vs.name AS serviceName,
                                vs.price AS unitPrice,
                                COUNT(*) AS count
                            FROM payment p
                            JOIN appointment a ON a.appointment_id = p.appointment_id
                            JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                            JOIN veterinary_service vs ON hvs.id_service = vs.service_id
                            WHERE p.status = 'COMPLETADA'
                              AND p.payment_date_time BETWEEN :startDate AND :endDate
                            GROUP BY TO_CHAR(TRUNC(p.payment_date_time, 'YYYY'), 'YYYY'), vs.name, vs.price
                            ORDER BY TO_CHAR(TRUNC(p.payment_date_time, 'YYYY'), 'YYYY'), vs.name
                        """, nativeQuery = true)
        List<IncomeByPeriodAndServiceDTO> findIncomeYearly(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                            SELECT
                                TO_CHAR(TRUNC(p.payment_date_time, 'MM'), 'MM/YYYY') AS period,
                                vs.name AS serviceName,
                                vs.price AS unitPrice,
                                COUNT(*) AS count
                            FROM payment p
                            JOIN appointment a ON a.appointment_id = p.appointment_id
                            JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                            JOIN veterinary_service vs ON hvs.id_service = vs.service_id
                            WHERE p.status = 'COMPLETADA'
                              AND p.payment_date_time BETWEEN :startDate AND :endDate
                            GROUP BY TO_CHAR(TRUNC(p.payment_date_time, 'MM'), 'MM/YYYY'), vs.name, vs.price
                            ORDER BY TO_CHAR(TRUNC(p.payment_date_time, 'MM'), 'MM/YYYY'), vs.name
                        """, nativeQuery = true)
        List<IncomeByPeriodAndServiceDTO> findIncomeMonthly(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                            SELECT
                                TO_CHAR(TRUNC(p.payment_date_time, 'DD'), 'DD/MM/YYYY') AS period,
                                vs.name AS serviceName,
                                vs.price AS unitPrice,
                                COUNT(*) AS count
                            FROM payment p
                            JOIN appointment a ON a.appointment_id = p.appointment_id
                            JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                            JOIN veterinary_service vs ON hvs.id_service = vs.service_id
                            WHERE p.status = 'COMPLETADA'
                              AND p.payment_date_time BETWEEN :startDate AND :endDate
                            GROUP BY TO_CHAR(TRUNC(p.payment_date_time, 'DD'), 'DD/MM/YYYY'), vs.name, vs.price
                            ORDER BY TO_CHAR(TRUNC(p.payment_date_time, 'DD'), 'DD/MM/YYYY'), vs.name
                        """, nativeQuery = true)
        List<IncomeByPeriodAndServiceDTO> findIncomeDaily(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                            SELECT
                                TO_CHAR(TRUNC(p.payment_date_time, 'IW'), 'IW/YYYY') AS period,
                                vs.name AS serviceName,
                                vs.price AS unitPrice,
                                COUNT(*) AS count
                            FROM payment p
                            JOIN appointment a ON a.appointment_id = p.appointment_id
                            JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                            JOIN veterinary_service vs ON hvs.id_service = vs.service_id
                            WHERE p.status = 'COMPLETADA'
                              AND p.payment_date_time BETWEEN :startDate AND :endDate
                            GROUP BY TO_CHAR(TRUNC(p.payment_date_time, 'IW'), 'IW/YYYY'), vs.name, vs.price
                            ORDER BY TO_CHAR(TRUNC(p.payment_date_time, 'IW'), 'IW/YYYY'), vs.name
                        """, nativeQuery = true)
        List<IncomeByPeriodAndServiceDTO> findIncomeWeekly(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}