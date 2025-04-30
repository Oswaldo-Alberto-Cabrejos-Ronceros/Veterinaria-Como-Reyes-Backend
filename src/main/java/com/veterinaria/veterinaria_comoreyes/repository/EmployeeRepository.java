package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.ClientListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUser(User user);

    boolean existsByPhone(String phone);

    boolean existsByDni(String dni);

    Optional<Employee> findByDni(String dni);

    @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.EmployeeListDTO(" +
            "e.employeeId, " +
            "e.dni, " +
            "e.name, " +
            "e.lastName, " +
            "e.headquarter.name, " +
            "CASE WHEN e.status = 1 THEN 'Activo' ELSE 'Bloqueado' END) " +
            "FROM Employee e " +
            "WHERE (:dni IS NULL OR e.dni = :dni) AND " +
            "(:name IS NULL OR e.name LIKE %:name%) AND " +
            "(:lastName IS NULL OR e.lastName LIKE %:lastName%) AND " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(:headquarterId IS NULL OR e.headquarter.headquarterId = :headquarterId)")
    Page<EmployeeListDTO> searchEmployees(@Param("dni") String dni,
                                          @Param("name") String name,
                                          @Param("lastName") String lastName,
                                          @Param("status") Byte status,
                                          @Param("headquarterId") Long headquarterId,
                                          Pageable pageable);
}
