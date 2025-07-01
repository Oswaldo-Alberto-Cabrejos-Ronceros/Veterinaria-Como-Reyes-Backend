package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IPaymentMethodService {
    PaymentMethodDTO getPaymentMethodById(Long id);

    List<PaymentMethodDTO> getAllPaymentMethods();

    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);

    PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO paymentMethodDTO);

    void deletePaymentMethod(Long id);

    void validePaymentMethod(Long id);

    @Transactional
    void activatePaymentMethod(Long paymentMethodId);

    Page<PaymentMethodListDTO> searchPaymentMethodsPage(String name, Boolean status, Pageable pageable);
}
