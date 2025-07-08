package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoForAppointmentDTO {
    private Long paymentId;
    private BigDecimal amount;
    private String serviceName;
    private Long paymentMethodId;
    private String paymentMethod;
    private String paymentStatus;
}
