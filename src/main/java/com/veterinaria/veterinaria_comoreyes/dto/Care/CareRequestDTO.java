package com.veterinaria.veterinaria_comoreyes.dto.Care;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareRequestDTO {

    @NotNull(message = "Care ID is required")
    private Long headquarterVetServiceId;

    @NotNull(message = "Animal ID is required")
    private Long animalId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Payment Method ID is required")
    private Long paymentMethodId;

}
