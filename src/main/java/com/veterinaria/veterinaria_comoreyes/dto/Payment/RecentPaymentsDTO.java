package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentPaymentsDTO {
    private Long paymentId;
    private String clientFullName;
    private String clientInitials;
    private String animalName;
    private String serviceName;
    private BigDecimal amount;
    private String paymentDate;
    private String paymentTime;
    private String paymentStatus;
}
