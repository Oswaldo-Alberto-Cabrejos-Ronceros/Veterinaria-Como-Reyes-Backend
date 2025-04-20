package com.veterinaria.veterinaria_comoreyes.dto;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long employeeId;

    @NotBlank(message = "El dni es obligatorio")
    @Size(min = 8, max = 8, message = "El dni debe tener 8 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El dni solo debe contener números")
    private String dni;

    @Null
    @Size(min = 8, max = 8, message = "El cmvp debe tener 8 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El cmvp solo debe contener números")
    private String cmvp;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "Nombre no puede tener más de 50 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "apellido no puede tener más de 50 caracteres")
    private String lastName;

    @Null
    @Size(max = 255, message = "La direccion no puede tener mas de 255 caracteres")
    private String address;

    @NotBlank(message = "El celular es obligatorio")
    @Size(min = 9, max = 9, message = "El celular debe tener 9 caracteres")
    @Pattern(regexp = "^[0-9]*$", message = "El celular solo debe contener números")
    private String phone;

    @NotBlank(message = "La direccion de imagen es obligatoria")
    private String dirImage="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgpnmY-O9iz09Jka-vGvK2Lv-U-pL3H18CfA&s";

    @NotNull(message = "La sede es obligatoria")
    private Headquarter headquarter;

    @Null
    private User user;

    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @NotNull(message = "El status es obligatorio")
    private Byte status;

}
