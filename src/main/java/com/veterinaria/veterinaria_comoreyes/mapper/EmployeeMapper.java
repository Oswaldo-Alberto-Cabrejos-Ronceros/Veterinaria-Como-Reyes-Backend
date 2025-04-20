package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.AuthenticationResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;

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
                employee.getDirImage(),
                employee.getHeadquarter(),
                employee.getUser(),
                employee.getRole(),
                employee.getStatus()
        );
    }

    public static Employee mapToEmployee(EmployeeDTO employeeDTO) {
        return new Employee(
                employeeDTO.getEmployeeId(),
                employeeDTO.getDni(),
                employeeDTO.getCmvp(),
                employeeDTO.getName(),
                employeeDTO.getLastName(),
                employeeDTO.getAddress(),
                employeeDTO.getPhone(),
                employeeDTO.getDirImage(),
                employeeDTO.getHeadquarter(),
                employeeDTO.getUser(),
                employeeDTO.getRole(),
                employeeDTO.getStatus()
        );
    }
    public static AuthenticationResponseDTO mapToAuthenticationResponseDTO(Long userId,EmployeeDTO employeeDTO,String jwtToken, String refreshToken) {
        return new AuthenticationResponseDTO(userId,employeeDTO.getName(),employeeDTO.getRole().getName(),jwtToken,refreshToken);
    }
}
