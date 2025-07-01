package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUser(UserDTO user);

    boolean existsByPhone(String phone);

    boolean existsByDni(String dni);

    Optional<Employee> findByDni(String dni);

    Optional<Employee> findByEmployeeIdAndStatusTrue(Long id);

    List<Employee> findByHeadquarter_HeadquarterIdAndStatusTrue(Long headquarterId);

    Employee findByEmployeeId(Long employeeId);

    @Query("SELECT e FROM Employee e WHERE e.user = :user")
    Employee findByUserForAuth(@Param("user") User user);

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeListDTO(
                    e.employeeId,
                    e.dni,
                    e.cmvp,
                    e.name,
                    e.lastName,
                    r.name,
                    h.name,
                    CASE WHEN e.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM Employee e
                JOIN e.roles r
                JOIN e.headquarter h
                WHERE (:dni IS NULL OR e.dni LIKE :dni)
                  AND (:cmvp IS NULL OR e.cmvp LIKE :cmvp)
                  AND (:lastname IS NULL OR e.lastName LIKE :lastname)
                  AND (:rolName IS NULL OR r.name LIKE :rolName)
                  AND (:nameHeadquarter IS NULL OR h.name LIKE :nameHeadquarter)
                  AND (:status IS NULL OR e.status = :status)
            """)
    Page<EmployeeListDTO> searchEmployeesWithFilters(
            @Param("dni") String dni,
            @Param("cmvp") String cmvp,
            @Param("lastname") String lastname,
            @Param("rolName") String rolName,
            @Param("nameHeadquarter") String nameHeadquarter,
            @Param("status") Boolean status,
            Pageable pageable);    

}
