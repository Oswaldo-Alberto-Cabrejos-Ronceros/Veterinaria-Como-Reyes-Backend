package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.*;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentStatus;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping
    public PaymentDTO createPayment(@Valid @RequestBody PaymentDTO dto) {
        return paymentService.createPayment(dto);
    }

    @PutMapping("/{id}")
    public PaymentDTO updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentDTO dto) {
        return paymentService.updatePayment(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PaymentListDTO>> getAllPaymentsForTable(Pageable pageable) {
        Page<PaymentListDTO> page = paymentService.getAllPaymentsForTable(pageable);
        return ResponseEntity.ok(page);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<PaymentListDTO>> search(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) Long headquarterId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate, // formato: yyyy-MM-dd
            @RequestParam(required = false) String endDate, // formato: yyyy-MM-dd
            Pageable pageable
    ) {
        var page = paymentService.searchPayments(dni, headquarterId, serviceId,
                status, startDate, endDate, pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{paymentId}/status/completed")
    public ResponseEntity<Void> setCompleted(@PathVariable Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, PaymentStatus.COMPLETADA);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}/status/cancelled")
    public ResponseEntity<Void> setCancelled(@PathVariable Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, PaymentStatus.CANCELADA);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}/status/pending")
    public ResponseEntity<Void> setPending(@PathVariable Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, PaymentStatus.PENDIENTE);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}/status/refunded")
    public ResponseEntity<Void> setRefunded(@PathVariable Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, PaymentStatus.REEMBOLSADA);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/panel-admin/stats")
    public ResponseEntity<PaymentStatsForPanelAdminDTO> getPaymentStats() {
        return ResponseEntity.ok(paymentService.getCompletedPaymentsStats());
    }
    @GetMapping("/panel-manager/stats/{headquarterId}")
    public ResponseEntity<PaymentStatsForPanelAdminDTO> getPaymentsStatsByHeadquarter(
            @PathVariable Long headquarterId) {
        return ResponseEntity.ok(paymentService.getPaymentsStatsByHeadquarter(headquarterId));
    }
    /************** Panel Receptionist ********/
    @GetMapping("/panel-receptionist/income-today")
    public ResponseEntity<IncomeStatsTodayDTO> getTodayIncomeStats() {
        return ResponseEntity.ok(paymentService.getTodayIncomeStats());
    }

    @GetMapping("/recent-completed/{headquarterId}")
    public ResponseEntity<List<RecentPaymentsDTO>> getRecentCompletedPayments(
            @PathVariable Long headquarterId) {
        List<RecentPaymentsDTO> data = paymentService.getRecentCompletedPayments(headquarterId);
        return ResponseEntity.ok(data);
    }

}
