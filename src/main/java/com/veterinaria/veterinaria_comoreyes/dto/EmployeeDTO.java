package com.veterinaria.veterinaria_comoreyes.dto;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long employeeId;


    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El DNI solo debe contener números")
    private String dni;

    @Size(min = 5, max = 5, message = "El CMVP debe tener 5 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El CMVP solo debe contener números")
    private String cmvp;

    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String name;

    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String lastName;

    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String address;

    @Size(min = 9, max = 9, message = "El celular debe tener 9 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El celular solo debe contener números")
    private String phone;

    private LocalDate birthDate;

    private String dirImage = "user.webp";

    @NotNull(message = "La sede es obligatoria")
    private Headquarter headquarter;

    private User user;

    private List<RoleDTO> roles;
}