package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {RoleMapper.class},
        componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "headquarter", target = "headquarter")
    EmployeeDTO mapToEmployeeDTO(Employee employee);

    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "headquarter", target = "headquarter")
    Employee mapToEmployee(EmployeeDTO dto);

    List<EmployeeDTO> mapToEmployeeDTOList(List<Employee> employeeList);

    List<Employee> mapToEmployeeList(List<EmployeeDTO> dtoList);
}
