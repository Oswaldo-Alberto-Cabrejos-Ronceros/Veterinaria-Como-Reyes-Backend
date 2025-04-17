package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PaymentMethodDTO createPaymentMethod(@RequestBody PaymentMethodDTO dto) {
        return paymentMethodService.createPaymentMethod(dto);
    }

    @PutMapping("/{id}")
    public PaymentMethodDTO updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodDTO dto) {
        return paymentMethodService.updatePaymentMethod(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
    }
}
