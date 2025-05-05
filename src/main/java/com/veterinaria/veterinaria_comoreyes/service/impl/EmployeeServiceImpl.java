package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.service.IRoleService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.HeadquarterUtil;
import com.veterinaria.veterinaria_comoreyes.util.JwtUtil;
import com.veterinaria.veterinaria_comoreyes.util.PhoneUtil;
import com.veterinaria.veterinaria_comoreyes.util.ReniecUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;
    private final IUserService userService;
    private final PhoneUtil phoneUtil;
    private final HeadquarterUtil headquarterUtil;
    private final ReniecUtil reniecUtil;
    private final IRoleService roleService;
    private final JwtUtil jwtUtil;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, IUserService userService, PhoneUtil phoneUtil, HeadquarterUtil headquarterUtil, ReniecUtil reniecUtil, IRoleService roleService, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        return EmployeeMapper.mapToEmployeeDTO(employee);
    }

    @Override
    public EmployeeDTO getEmployeeByUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return EmployeeMapper.mapToEmployeeDTOList(employees);
    }

    @Transactional
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        //validacines del telefono
        phoneUtil.validatePhoneAvailable(employeeDTO.getPhone(), "empleado");

        //Validar si hay otro dni registrado ya
        if (employeeRepository.existsByDni(employeeDTO.getDni())) {
            throw new RuntimeException("Ya existe un empleado con ese DNI: " + employeeDTO.getDni());
        }

        //Validar Data con la Reniec ( los nombre y apellidos coincidan con el dni)
        reniecUtil.validateData(
                employeeDTO.getDni(),
                employeeDTO.getName(),
                employeeDTO.getLastName()
        );

        //validar Sede
        headquarterUtil.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());

        //Agregar Usuario si tiene credenciales
        if (employeeDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("E");
            userDTO.setEmail(employeeDTO.getUser().getEmail());
            userDTO.setPassword(employeeDTO.getUser().getPassword());

            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = UserMapper.maptoUser(savedUserDTO);
            employeeDTO.setUser(savedUser);
        }

        //Mapeamos el empleadoDTO en la entidad empleado
        Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);

        // Procesar roles
        if (employeeDTO.getRoles() != null && !employeeDTO.getRoles().isEmpty()) {
            List<Role> roles = roleService.validateAndFetchRoles(employeeDTO.getRoles());
            employee.setRoles(roles); // si usas mapping manual
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(savedEmployee);
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

        // Procesar roles solo si se proporcionan explícitamente


        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return EmployeeMapper.mapToEmployeeDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        //logica para eliminar el empleado y tos las filas relacionadas a el
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
        return EmployeeMapper.mapToEmployeeDTO(employee);
    }

    //DEBERIA FUNCIONAR CON EN PACTH para solo cambiar el estado
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
    public Page<EmployeeListDTO> searchEmployees(String dni, String name, String lastName, Byte status, Long headquarterId, Pageable pageable) {
        return employeeRepository.searchEmployees(dni, name, lastName, status, headquarterId, pageable);
    }

    @Override
    public MyInfoEmployeeDTO myInfoAsEmployee(String token, Long id) {

        // Extraer el ID real del JWT
        Long employeeIdFromToken = Long.valueOf(jwtUtil.getIdFromJwt(token));

        // Verificar que el ID enviado por el frontend coincida con el del token
        if (!employeeIdFromToken.equals(id)) {
            throw new RuntimeException("No tienes permiso para acceder a esta información.");
        }


        // Buscar al empleado por su ID
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Empleado no encontrado con id: " + id);
        }

        // Construir el DTO
        MyInfoEmployeeDTO dto = new MyInfoEmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setUserId(employee.getUser().getUserId());
        dto.setDni(employee.getDni());
        dto.setCmvp(employee.getCmvp());
        dto.setNames(employee.getName());
        dto.setLastNames(employee.getLastName());
        dto.setAddress(employee.getAddress());
        dto.setPhone(employee.getPhone());
        dto.setBirthDate(employee.getBirthDate()); // Asegúrate que es un LocalDate, si no, adapta esto
        dto.setDirImage(employee.getDirImage());
        dto.setHeadquarterName(employee.getHeadquarter().getName());

        // Obtener el rol con menor posición (más cercano a 0)
        String mainRole = employee.getRoles().stream()
                .min(Comparator.comparingInt(role -> role.getPosition()))
                .map(Role::getName)
                .orElse("Sin rol");

        dto.setMainRole(mainRole);

        return dto;
    }
}

