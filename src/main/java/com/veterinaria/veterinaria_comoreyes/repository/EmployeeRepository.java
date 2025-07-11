package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  @Query(value = """
      SELECT e.employee_id as employeeId, e.dni, e.cmvp, e.name, e.last_name as lastName,
             r.name as rolName, h.name as nameHeadquarter,
             CASE WHEN e.status = 1 THEN 'Activo' ELSE 'Inactivo' END as status
      FROM employee e
      JOIN employee_role er ON e.employee_id = er.id_employee
      JOIN role r ON r.role_id = er.id_role
      JOIN headquarter h ON h.headquarter_id = e.id_headquarter
      WHERE (:dni IS NULL OR e.dni LIKE :dni || '%')
        AND (:cmvp IS NULL OR e.cmvp LIKE :cmvp || '%')
        AND (:lastname IS NULL OR e.last_name LIKE :lastname || '%')
        AND (:name IS NULL OR e.name LIKE :name || '%')
        AND (:rolName IS NULL OR r.name LIKE :rolName || '%')
        AND (:nameHeadquarter IS NULL OR h.name LIKE :nameHeadquarter || '%')
        AND (:status IS NULL OR e.status = :status)
      ORDER BY e.employee_id DESC
      """, countQuery = """
      SELECT COUNT(*)
      FROM employee e
      JOIN employee_role er ON e.employee_id = er.id_employee
      JOIN role r ON r.role_id = er.id_role
      JOIN headquarter h ON h.headquarter_id = e.id_headquarter
      WHERE (:dni IS NULL OR e.dni LIKE :dni || '%')
        AND (:cmvp IS NULL OR e.cmvp LIKE :cmvp || '%')
        AND (:lastname IS NULL OR e.last_name LIKE :lastname || '%')
        AND (:name IS NULL OR e.name LIKE :name || '%')
        AND (:rolName IS NULL OR r.name LIKE :rolName || '%')
        AND (:nameHeadquarter IS NULL OR h.name LIKE :nameHeadquarter || '%')
        AND (:status IS NULL OR e.status = :status)
      """, nativeQuery = true)
  Page<EmployeeListDTO> searchEmployeesWithFilters(
      @Param("dni") String dni,
      @Param("cmvp") String cmvp,
      @Param("lastname") String lastname,
      @Param("name") String name,
      @Param("rolName") String rolName,
      @Param("nameHeadquarter") String nameHeadquarter,
      @Param("status") Boolean status,
      Pageable pageable);

  @Modifying
  @Transactional
  @Query(value = """
          UPDATE employee
          SET status = 0, block_reason = :reason
          WHERE employee_id = :employeeId
      """, nativeQuery = true)
  void blockEmployee(@Param("employeeId") Long employeeId, @Param("reason") String reason);

}
