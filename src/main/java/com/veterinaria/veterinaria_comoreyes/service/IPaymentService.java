package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.*;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPaymentService {

    PaymentDTO createPayment(PaymentDTO dto);

    PaymentDTO getPaymentById(Long id);

    List<PaymentDTO> getAllPayments();

    PaymentDTO updatePayment(Long id, PaymentDTO dto);

    void deletePayment(Long id);

    Page<PaymentListDTO> getAllPaymentsForTable(Pageable pageable);

    Page<PaymentListDTO> searchPayments(String dni, Long headquarterId, Long serviceId,
                                        String status, String startDate, String endDate,
                                        Pageable pageable);

    @Transactional
    void updatePaymentStatus(Long paymentId, PaymentStatus status);

    PaymentStatsForPanelAdminDTO getCompletedPaymentsStats();

    PaymentStatsForPanelAdminDTO getPaymentsStatsByHeadquarter(Long headquarterId);

    IncomeStatsTodayDTO getTodayIncomeStats();

    List<RecentPaymentsDTO> getRecentCompletedPayments(Long headquarterId);
}
