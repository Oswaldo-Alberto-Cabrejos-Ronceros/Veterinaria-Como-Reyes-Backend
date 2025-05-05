package com.veterinaria.veterinaria_comoreyes.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoEmployeeDTO {

    private Long employeeId;
    private Long userId;
    private String dni;
    private String cmvp;
    private String names;
    private String lastNames;
    private String address;
    private String phone;
    private String headquarterName;
    private LocalDate birthDate;
    private String dirImage;
    private String mainRole;

}
