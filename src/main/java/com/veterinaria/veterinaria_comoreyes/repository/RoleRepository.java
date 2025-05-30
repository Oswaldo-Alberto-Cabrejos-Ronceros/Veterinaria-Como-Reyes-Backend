package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Obtiene la informacion de un rol qu exita y que su status sea true
     */
    Optional<Role> findByRoleIdAndStatusIsTrue(Long id);

    /**
     * Cuenta cuántos roles con status=true están asignados a un empleado.
     */
    long countByEmployeesEmployeeIdAndStatusTrue(Long employeeId);

    /**
     * Recupera la lista de roles con status=true para un empleado.
     */
    List<Role> findByEmployeesEmployeeIdAndStatusTrue(Long employeeId);
    Optional<Role> findByName(String nameRole);
}
