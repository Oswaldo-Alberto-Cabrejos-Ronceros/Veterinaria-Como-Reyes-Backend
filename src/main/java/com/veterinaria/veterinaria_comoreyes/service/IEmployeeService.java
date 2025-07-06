package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.MyInfoEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.nMyInfoEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface IEmployeeService {
    EmployeeDTO getEmployeeById(Long id);

    EmployeeDTO getEmployeeByUser(UserDTO userDTO);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    void deleteEmployee(Long id);

    // Buscar empleado por DNI
    EmployeeDTO getEmployeeByDni(String dni);

    // Restaurar un empleado eliminado lógicamente
    void restoreEmployee(Long employeeId);

        // Asignar roles a un empleado
    EmployeeDTO assignRolesToEmployee(Long employeeId, List<Long> roleIds);

    // Remover roles de un empleado
    EmployeeDTO removeRolesFromEmployee(Long employeeId, List<Long> roleIds);

    EmployeeDTO addRoleToEmployee(Long employeeId, Long roleId);


    // Método para búsqueda personalizada
    Page<EmployeeListDTO> searchEmployees(String dni, String cmvp, String lastname, String rolName,
            String nameHeadquarter, Boolean status,
            Pageable pageable);

    nMyInfoEmployeeDTO myInfoAsEmployee(Long id);

    String getMainRoleName(Long employeeId);

    // METODOS DE EMPLOYEE FOR AUTH
    Employee getEmployeeByUserForAuth(User user);

    void blockEmployee(Long employeeId, String reason);
}
