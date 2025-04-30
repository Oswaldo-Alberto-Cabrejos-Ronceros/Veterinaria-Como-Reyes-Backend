package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.repository.RoleRepository;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.service.IRoleService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.HeadquarterUtil;
import com.veterinaria.veterinaria_comoreyes.util.PhoneUtil;
import com.veterinaria.veterinaria_comoreyes.util.ReniecUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, IUserService userService, PhoneUtil phoneUtil, HeadquarterUtil headquarterUtil, ReniecUtil reniecUtil, IRoleService roleService) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
        this.roleService = roleService;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        return null;
    }

    @Override
    public EmployeeDTO getEmployeeByUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return List.of();
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
            userDTO.setStatus((byte) 1);

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
        existingEmployee.setStatus(employeeDTO.getStatus());

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

        employee.setStatus((byte) 0);

        if (employee.getUser() != null) {
            employee.getUser().setStatus((byte) 0);
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

        employee.setStatus((byte) 1);

        if (employee.getUser() != null) {
            employee.getUser().setStatus((byte) 1);
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
}

