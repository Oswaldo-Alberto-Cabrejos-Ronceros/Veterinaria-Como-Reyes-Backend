package com.veterinaria.veterinaria_comoreyes.dto.Employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    String rolName;
    String nameHeadquarter;
    String status;
}