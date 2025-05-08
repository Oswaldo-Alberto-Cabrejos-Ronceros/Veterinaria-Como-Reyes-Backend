package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.service.IRoleService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IUserService userService;
    private final PhoneUtil phoneUtil;
    private final HeadquarterUtil headquarterUtil;
    private final ReniecUtil reniecUtil;
    private final IRoleService roleService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            IUserService userService,
            PhoneUtil phoneUtil,
            HeadquarterUtil headquarterUtil,
            ReniecUtil reniecUtil,
            IRoleService roleService,
            JwtTokenUtil jwtTokenUtil,
            UserMapper userMapper,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
        this.roleService = roleService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        return employeeMapper.mapToEmployeeDTO(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.mapToEmployeeDTOList(employees);
    }

    @Transactional
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        phoneUtil.validatePhoneAvailable(employeeDTO.getPhone(), "empleado");

        if (employeeRepository.existsByDni(employeeDTO.getDni())) {
            throw new RuntimeException("Ya existe un empleado con ese DNI: " + employeeDTO.getDni());
        }

        reniecUtil.validateData(
                employeeDTO.getDni(),
                employeeDTO.getName(),
                employeeDTO.getLastName());

        headquarterUtil.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());

        if (employeeDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("E");
            userDTO.setEmail(employeeDTO.getUser().getEmail());
            userDTO.setPassword(employeeDTO.getUser().getPassword());

            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = userMapper.maptoUser(savedUserDTO); // USO CORRECTO
            employeeDTO.setUser(savedUser);
        }

        Employee employee = employeeMapper.mapToEmployee(employeeDTO);

        if (employeeDTO.getRoles() != null && !employeeDTO.getRoles().isEmpty()) {
            List<Role> roles = roleService.validateAndFetchRoles(employeeDTO.getRoles());
            employee.setRoles(roles);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDTO(savedEmployee);
    }

    @Transactional
    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        phoneUtil.validatePhoneAvailable(employeeDTO.getPhone(), "empleado");

        if (!existingEmployee.getDni().equals(employeeDTO.getDni()) &&
                employeeRepository.existsByDni(employeeDTO.getDni())) {
            throw new RuntimeException("Ya existe otro empleado con ese DNI: " + employeeDTO.getDni());
        }

        headquarterUtil.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());
        existingEmployee.getHeadquarter().setHeadquarterId(employeeDTO.getHeadquarter().getHeadquarterId());

        existingEmployee.setDni(employeeDTO.getDni());
        existingEmployee.setCmvp(employeeDTO.getCmvp());
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setAddress(employeeDTO.getAddress());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setBirthDate(employeeDTO.getBirthDate());
        existingEmployee.setDirImage(employeeDTO.getDirImage());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.mapToEmployeeDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        // lógica de eliminación si aplica
    }

    @Override
    public void blockEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        employee.setStatus(false);
        if (employee.getUser() != null) {
            employee.getUser().setStatus(false);
        }

        employeeRepository.save(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByDni(String dni) {
        Employee employee = employeeRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con DNI: " + dni));
        return employeeMapper.mapToEmployeeDTO(employee);
    }

    @Override
    public void restoreEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        employee.setStatus(true);
        if (employee.getUser() != null) {
            employee.getUser().setStatus(true);
        }

        employeeRepository.save(employee);
    }

    @Override
    public EmployeeDTO assignRolesToEmployee(Long employeeId, List<Long> roleIds) {
        return null;
    }

    @Override
    public EmployeeDTO removeRolesFromEmployee(Long employeeId, List<Long> roleIds) {
        return null;
    }

    @Override
    public EmployeeDTO addRoleToEmployee(Long employeeId, Long roleId) {
        return null;
    }

    @Override
    public Page<EmployeeListDTO> searchEmployees(String dni, String name, String lastName, Byte status,
            Long headquarterId, Pageable pageable) {
        return employeeRepository.searchEmployees(dni, name, lastName, status, headquarterId, pageable);
    }

    @Override
    public MyInfoEmployeeDTO myInfoAsEmployee(String token, Long id) {
        Long employeeIdFromToken = Long.valueOf(jwtTokenUtil.getEntityIdFromJwt(token));

        if (!employeeIdFromToken.equals(id)) {
            throw new RuntimeException("No tienes permiso para acceder a esta información.");
        }

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Empleado no encontrado con id: " + id);
        }

        MyInfoEmployeeDTO dto = new MyInfoEmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setUserId(employee.getUser().getUserId());
        dto.setDni(employee.getDni());
        dto.setCmvp(employee.getCmvp());
        dto.setNames(employee.getName());
        dto.setLastNames(employee.getLastName());
        dto.setAddress(employee.getAddress());
        dto.setPhone(employee.getPhone());
        dto.setBirthDate(employee.getBirthDate());
        dto.setDirImage(employee.getDirImage());
        dto.setHeadquarterName(employee.getHeadquarter().getName());

        String mainRole = employee.getRoles().stream()
                .min(Comparator.comparingInt(Role::getPosition))
                .map(Role::getName)
                .orElse("Sin rol");

        dto.setMainRole(mainRole);

        return dto;
    }

    @Override
    public List<String> getEmployeePermissions(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        return employee.getRoles().stream()
                .filter(role -> Boolean.TRUE.equals(role.getStatus()))
                .flatMap(role -> role.getPermissions().stream()
                        .filter(permission -> Boolean.TRUE.equals(permission.getStatus())))
                .map(Permission::getActionCode)
                .distinct()
                .collect(Collectors.toList());
    }
}
