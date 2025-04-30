package com.veterinaria.veterinaria_comoreyes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentMethodDTO {

    private long paymentMethodId;

    @NotBlank(message = "El nombre del método de pago es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String description;
}
