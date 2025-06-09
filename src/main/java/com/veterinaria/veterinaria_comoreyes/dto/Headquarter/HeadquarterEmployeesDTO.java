package com.veterinaria.veterinaria_comoreyes.dto.Headquarter;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeInfoPublicDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterEmployeesDTO {
    String  nameHeadquarter;
    List<EmployeeInfoPublicDTO> employees;
}
