package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.AuthenticationResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;

import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDTO mapToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getDni(),
                employee.getCmvp(),
                employee.getName(),
                employee.getLastName(),
                employee.getAddress(),
                employee.getPhone(),
                employee.getBirthDate(),
                employee.getDirImage(),
                employee.getHeadquarter(),
                employee.getUser(),
                employee.getStatus()
        );
    }

    public static Employee mapToEmployee(EmployeeDTO dto) {
        return new Employee(
                dto.getEmployeeId(),
                dto.getDni(),
                dto.getCmvp(),
                dto.getName(),
                dto.getLastName(),
                dto.getAddress(),
                dto.getPhone(),
                dto.getBirthDate(),
                dto.getDirImage(),
                dto.getHeadquarter(),
                dto.getUser(),
                dto.getStatus()
        );
    }


    /*
    public static AuthenticationResponseDTO mapToAuthenticationResponseDTO(Long userId,EmployeeDTO employeeDTO,String jwtToken, String refreshToken) {
        return new AuthenticationResponseDTO(userId,employeeDTO.getName(),employeeDTO.getRole().getName(),jwtToken,refreshToken);
    }

     */
}
