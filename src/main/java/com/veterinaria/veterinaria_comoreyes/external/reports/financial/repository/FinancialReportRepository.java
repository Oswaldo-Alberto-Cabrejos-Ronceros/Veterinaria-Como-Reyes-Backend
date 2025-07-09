package com.veterinaria.veterinaria_comoreyes.external.reports.financial.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.veterinaria.veterinaria_comoreyes.entity.Payment;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentStatus;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByPeriodDTO;

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
}