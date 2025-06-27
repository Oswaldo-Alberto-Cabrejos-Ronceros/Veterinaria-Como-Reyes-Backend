package com.veterinaria.veterinaria_comoreyes.dto.Payment_Method;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicInfoPaymentMethodDTO {
    private Long paymentMethodId;
    private String name;
}
