package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.nMyInfoEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Role.RoleBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserEmailDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
import com.veterinaria.veterinaria_comoreyes.exception.PhoneAlreadyExistsException;
import com.veterinaria.veterinaria_comoreyes.external.reniec.service.IReniecService;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import com.veterinaria.veterinaria_comoreyes.service.IRoleService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IUserService userService;
    private final IHeadquarterService headquarterService;
    private final IReniecService reniecService;
    private final IRoleService roleService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            IUserService userService,
            IHeadquarterService headquarterService,
            IReniecService reniecService,
            IRoleService roleService,
            JwtTokenUtil jwtTokenUtil,
            UserMapper userMapper,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.headquarterService = headquarterService;
        this.reniecService = reniecService;
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

        Employee employee = employeeRepository
                .findByUser(userDTO)
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado para el usuario ID: " + userDTO.getUserId()));

        return employeeMapper.mapToEmployeeDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.mapToEmployeeDTOList(employees);
    }

    @Transactional
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        validatePhoneAvailable(employeeDTO.getPhone());

        if (employeeRepository.existsByDni(employeeDTO.getDni())) {
            throw new RuntimeException("Ya existe un empleado con ese DNI: " + employeeDTO.getDni());
        }

        // validate that name and last Name match with data of Reniec
        reniecService.validateIdentityReniec(
                employeeDTO.getDni(),
                employeeDTO.getName(),
                employeeDTO.getLastName());

        headquarterService.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());

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

    private void validatePhoneAvailable(String phone) {
        boolean exist = employeeRepository.existsByPhone(phone);
        if (exist) {
            throw new PhoneAlreadyExistsException("El número de teléfono ya está registrado en otro cliente");
        }
    }

    private void validateDniAvailable(String dni) {
        boolean exist = employeeRepository.existsByDni(dni);
        if (exist) {
            throw new RuntimeException("Ya existe otro empleado con ese DNI");
        }
    }

    @Transactional
    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {

        // verificar si el cliente id ingresado existe
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        // verificar si ha ingresado otro numero y lo validamos si es el caso
        if (!existingEmployee.getPhone().equals(employeeDTO.getPhone())) {
            validatePhoneAvailable(employeeDTO.getPhone());
        }

        // verificar si ha ingresado un dni distinto y lo validmos si es el caso
        if (!existingEmployee.getDni().equals(employeeDTO.getDni())) {

            // validar que no exista otro cliente con el nuevo dni
            validateDniAvailable(employeeDTO.getDni());

            // validate that name and last Name match with data of Reniec
            reniecService.validateIdentityReniec(
                    employeeDTO.getDni(),
                    employeeDTO.getName(),
                    employeeDTO.getLastName());
        }

        headquarterService.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());

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

    /******************************************
     * Services user-employee
     ****************************************/
    @Override
    public nMyInfoEmployeeDTO myInfoAsEmployee(Long id) {

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Empleado no encontrado con id: " + id);
        }

        nMyInfoEmployeeDTO infoEmployeeDTO = new nMyInfoEmployeeDTO();
        infoEmployeeDTO.setEmployeeId(employee.getEmployeeId());
        infoEmployeeDTO.setNames(employee.getName());
        infoEmployeeDTO.setLastNames(employee.getLastName());
        infoEmployeeDTO.setDni(employee.getDni());
        infoEmployeeDTO.setPhone(employee.getPhone());
        infoEmployeeDTO.setCmvp(employee.getCmvp());
        infoEmployeeDTO.setAddress(employee.getAddress());
        infoEmployeeDTO.setDirImage(employee.getDirImage());
        infoEmployeeDTO.setBirthDate(employee.getBirthDate());

        if (employee.getUser() != null) {
            UserEmailDTO user = new UserEmailDTO();
            user.setEmail(employee.getUser().getEmail());
            user.setId(employee.getUser().getUserId());
            infoEmployeeDTO.setUser(user);
        }
        /*
         * else{
         * infoEmployeeDTO.setUser(null);
         * }
         */

        if (employee.getRoles() != null) {
            List<RoleBasicDTO> roles = new ArrayList<RoleBasicDTO>();
            roles = roleService.filterRolesStatusActive(employee.getRoles());
            infoEmployeeDTO.setRoles(roles);
        }
        if (employee.getHeadquarter() != null) {
            HeadquarterBasicDTO headquarterBasic = new HeadquarterBasicDTO();
            headquarterBasic.setId(employee.getHeadquarter().getHeadquarterId());
            headquarterBasic.setName(employee.getHeadquarter().getName());
            infoEmployeeDTO.setHeadquarter(headquarterBasic);
        }
        return infoEmployeeDTO;
    }

    @Override
    public String getMainRoleName(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        return employee.getRoles().stream()
                .filter(role -> Boolean.TRUE.equals(role.getStatus()))
                .min(Comparator.comparingInt(Role::getPosition))
                .map(Role::getName)
                .orElse(null);
    }

    @Override
    public Employee getEmployeeByUserForAuth(User user) {
        Employee employee = employeeRepository.findByUserForAuth(user);
        if (employee == null) {
            throw new AuthException("Empleado no encontrado", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeListDTO> searchEmployees(String dni, String cmvp, String lastname,
                                                 String name, String rolName, String nameHeadquarter,
                                                 Boolean status, Pageable pageable) {
        return employeeRepository.searchEmployeesWithFilters(
                dni, cmvp, lastname, name, rolName, nameHeadquarter, status, pageable);
    }

    @Override
    public void blockEmployee(Long employeeId, String reason) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));
        userService.blockUser(employee.getUser().getUserId());
        employeeRepository.blockEmployee(employeeId, reason);

    }


}
