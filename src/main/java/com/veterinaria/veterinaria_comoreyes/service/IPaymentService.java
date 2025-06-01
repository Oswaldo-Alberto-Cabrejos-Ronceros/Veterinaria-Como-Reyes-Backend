package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;

import java.util.List;

public interface IPaymentService {

    PaymentDTO createPayment(PaymentDTO dto);

    PaymentDTO getPaymentById(Long id);

    List<PaymentDTO> getAllPayments();

    PaymentDTO updatePayment(Long id, PaymentDTO dto);

    void deletePayment(Long id);
}
