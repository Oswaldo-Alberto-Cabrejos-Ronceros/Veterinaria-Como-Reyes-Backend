package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying
    @Query("UPDATE Role r SET r.status = true WHERE r.roleId = :roleId")
    void activateRole(@Param("roleId") Long roleId);
}
