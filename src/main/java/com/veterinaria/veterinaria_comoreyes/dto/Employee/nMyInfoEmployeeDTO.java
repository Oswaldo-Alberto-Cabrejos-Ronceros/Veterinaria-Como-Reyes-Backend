package com.veterinaria.veterinaria_comoreyes.dto.Employee;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Role.RoleBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserEmailDTO;
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
public class nMyInfoEmployeeDTO {

    private Long employeeId;
    private UserEmailDTO user;
    private String dni;
    private String cmvp;
    private String names;
    private String lastNames;
    private String address;
    private String phone;
    private HeadquarterBasicDTO headquarter;
    private LocalDate birthDate;
    private String dirImage;
    private List<RoleBasicDTO> roles;

}
