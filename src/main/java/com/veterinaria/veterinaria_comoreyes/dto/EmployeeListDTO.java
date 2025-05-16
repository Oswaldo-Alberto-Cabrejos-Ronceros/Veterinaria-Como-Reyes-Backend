package com.veterinaria.veterinaria_comoreyes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListDTO {

    Long employeeId;
    String dni;
    String cmvp;
    String name;
    String lastName;
    String nameHeadquarter;
    String status;

}
