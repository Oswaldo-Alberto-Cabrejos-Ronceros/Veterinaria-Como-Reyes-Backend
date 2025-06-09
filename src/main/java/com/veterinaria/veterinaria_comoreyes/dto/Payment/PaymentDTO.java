package com.veterinaria.veterinaria_comoreyes.dto.Payment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

     private Long paymentId;

    @NotNull(message = "El monto del pago es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double amount;

    @Positive(message = "La fecha y hora del pago es obligatoria")
    @PastOrPresent(message = "La fecha de pago no puede estar en el futuro")
    @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
    private LocalDateTime paymentDateTime;

    @NotNull(message = "El estado del pago es obligatorio")
    private PaymentStatus status;

    @NotNull(message = "Debe asociarse a una cita (appointmentId)")
    private Long appointmentId;

    // Opcional, pero si se envía, debe ser válido
    @Positive(message = "El ID de atención debe ser mayor a 0")
    private Long careId;

    @NotNull(message = "El método de pago es obligatorio")
    private Long paymentMethodId;
}
