package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodDTO;
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
}
