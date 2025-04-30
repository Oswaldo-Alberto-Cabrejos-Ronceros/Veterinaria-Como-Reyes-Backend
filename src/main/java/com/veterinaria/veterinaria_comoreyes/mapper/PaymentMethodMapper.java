package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentMethod;

public class PaymentMethodMapper {

    public static PaymentMethodDTO maptoPaymentMethodDTO(PaymentMethod paymentMethod) {
        return new PaymentMethodDTO(
                paymentMethod.getPaymentMethodId(),
                paymentMethod.getName(),
                paymentMethod.getDescription()
        );
    }

    public static PaymentMethod maptoPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        return new PaymentMethod(
                paymentMethodDTO.getPaymentMethodId(),
                paymentMethodDTO.getName(),
                paymentMethodDTO.getDescription()
        );
    }

}
