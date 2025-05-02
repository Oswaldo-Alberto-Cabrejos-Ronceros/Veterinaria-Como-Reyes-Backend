package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.AuthenticationResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDTO mapToEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        employeeDTO.setDni(employee.getDni());
        employeeDTO.setCmvp(employee.getCmvp());
        employeeDTO.setName(employee.getName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setDirImage(employee.getDirImage());
        employeeDTO.setMainRole(employee.getMainRole());
        employeeDTO.setStatus(employee.getStatus());

        // Mapear relaciones
        if (employee.getHeadquarter() != null) {
            employeeDTO.setHeadquarter(employee.getHeadquarter());
        }

        if (employee.getUser() != null) {
            employeeDTO.setUser(employee.getUser());
        }

        if (employee.getRoles() != null) {
            employeeDTO.setRoles(RoleMapper.mapToRoleDTOList(employee.getRoles()));
        }

        return employeeDTO;
    }

    public static Employee mapToEmployee(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setDni(dto.getDni());
        employee.setCmvp(dto.getCmvp());
        employee.setName(dto.getName());
        employee.setLastName(dto.getLastName());
        employee.setAddress(dto.getAddress());
        employee.setPhone(dto.getPhone());
        employee.setBirthDate(dto.getBirthDate());
        employee.setDirImage(dto.getDirImage());
        employee.setMainRole(dto.getMainRole());
        employee.setStatus(dto.getStatus());

        // Relaciones
        if (dto.getHeadquarter() != null) {
            employee.setHeadquarter(dto.getHeadquarter());
        }

        if (dto.getUser() != null) {
            employee.setUser(dto.getUser());
        }

        if (dto.getRoles() != null) {
            employee.setRoles(RoleMapper.mapToRoleList(dto.getRoles()));
        }

        return employee;
    }

    public static List<EmployeeDTO> mapToEmployeeDTOList(List<Employee> employeeList) {
        if (employeeList == null) return new ArrayList<>();

        return employeeList.stream()
                .map(EmployeeMapper::mapToEmployeeDTO)
                .collect(Collectors.toList());
    }

    public static List<Employee> mapToEmployeeList(List<EmployeeDTO> dtoList) {
        if (dtoList == null) return new ArrayList<>();

        return dtoList.stream()
                .map(EmployeeMapper::mapToEmployee)
                .collect(Collectors.toList());
    }
}
