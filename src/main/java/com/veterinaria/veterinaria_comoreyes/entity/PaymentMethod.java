package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
    private long paymentMethodId;
    private String name;
    private String description;
    private Integer status;
}
