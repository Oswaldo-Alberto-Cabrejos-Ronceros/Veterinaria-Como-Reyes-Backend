package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentRequestDTO {

    @NotNull(message = "La fecha y hora de la cita no puede ser nula")
    @FutureOrPresent(message = "La cita debe estar en el presente o futuro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduleDateTime;

    @Size(max = 255)
    private String comment;


    @Size(max = 255)
    private String cancellationNote =null;

    private StatusAppointment statusAppointment;

    @NotNull(message = "El ID del servicio veterinario es obligatorio")
    private Long headquarterVetServiceId;

    private Long assignedEmployeeId;

    @NotNull(message = "El ID del animal es obligatorio")
    private Long animalId;

    @NotNull(message = "El metodo de pago es obligatorio")
    private Long paymentMethodId;
}
