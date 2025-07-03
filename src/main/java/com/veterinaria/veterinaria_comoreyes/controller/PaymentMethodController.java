package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodListDTO;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private IPaymentMethodService paymentMethodService;

    @GetMapping
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @GetMapping("/{id}")
    public PaymentMethodDTO getPaymentMethodById(@PathVariable Long id) {
        return paymentMethodService.getPaymentMethodById(id);
    }

    @PostMapping
    public PaymentMethodDTO createPaymentMethod(@Valid @RequestBody PaymentMethodDTO dto) {
        return paymentMethodService.createPaymentMethod(dto);
    }

    @PutMapping("/{id}")
    public PaymentMethodDTO updatePaymentMethod(@PathVariable Long id, @Valid @RequestBody PaymentMethodDTO dto) {
        return paymentMethodService.updatePaymentMethod(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
    }

    @PutMapping("/{paymentMethodId}/activate")
    public ResponseEntity<Void> activatePaymentMethod(@PathVariable Long paymentMethodId) {
        paymentMethodService.activatePaymentMethod(paymentMethodId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public Page<PaymentMethodListDTO> searchPaymentMethods(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean status,
            @PageableDefault(size = 10) Pageable pageable) {
        return paymentMethodService.searchPaymentMethodsPage(name, status, pageable);
    }
}
