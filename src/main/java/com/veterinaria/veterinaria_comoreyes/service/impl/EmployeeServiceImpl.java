package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found with id: " + id));
        return EmployeeMapper.mapToEmployeeDTO(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByUser(UserDTO userDTO) {
        Employee employee = employeeRepository.findByUser(UserMapper.maptoUser(userDTO)).orElseThrow(()->new RuntimeException("Employee not found with user: " + userDTO.getUserId()));
        return EmployeeMapper.mapToEmployeeDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(EmployeeMapper::mapToEmployeeDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(savedEmployee);
    }

    @Transactional
    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employeeFound = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found with id: " + id));
        employeeFound.setDni(employeeDTO.getDni());
        employeeFound.setCmvp(employeeDTO.getCmvp());
        employeeFound.setName(employeeDTO.getName());
        employeeFound.setLastName(employeeDTO.getLastName());
        employeeFound.setAddress(employeeDTO.getAddress());
        employeeFound.setPhone(employeeDTO.getPhone());
        employeeFound.setDirImage(employeeDTO.getDirImage());
        employeeFound.setHeadquarter(employeeDTO.getHeadquarter());
        employeeFound.setRole(employeeDTO.getRole());
        employeeFound.setStatus(employeeDTO.getStatus());
        Employee updatedEmployee = employeeRepository.save(employeeFound);
        return EmployeeMapper.mapToEmployeeDTO(updatedEmployee);
    }
    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("Employee not found with id: " + id));
        employee.setStatus( (byte) 0);
        employeeRepository.save(employee);
    }
}
