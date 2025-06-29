package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentListDTO {
    private Long id;
    private String headquarterName;
    private String serviceName;
    private String clientDni;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private String paymentDate;
}
