package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.MyInfoEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Employee.nMyInfoEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Search employee for id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // New employee with user optional
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employee) {
        EmployeeDTO employeeCreated = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeCreated);
    }

    // Update complete employee data
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
        EmployeeDTO employeeUpdated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(employeeUpdated);
    }


    // changes status to active
    @PatchMapping("/{id}/restore")
    public ResponseEntity<?> restoreEmployee(@PathVariable Long id) {
        employeeService.restoreEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * @DeleteMapping("/{id}")
     * public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
     * employeeService.deleteEmployee(id);
     * return ResponseEntity.noContent().build();
     * }
     */

    // Listing and searching employees for the table with pagination

    @GetMapping("/{id}/myInfo")
    public ResponseEntity<nMyInfoEmployeeDTO> getEmployeeMyInfo(@PathVariable Long id) {
        nMyInfoEmployeeDTO myInfoEmployee = employeeService.myInfoAsEmployee(id);
        return ResponseEntity.ok(myInfoEmployee);

    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeListDTO>> searchEmployees(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String cmvp,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String rolName,
            @RequestParam(required = false) String nameHeadquarter,
            @RequestParam(required = false) Boolean status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(employeeService.searchEmployees(
                dni, cmvp, lastname, rolName, nameHeadquarter, status, pageable));
    }

    @PatchMapping("/{employeeId}/block")
    public ResponseEntity<String> blockEmployee(
            @PathVariable Long employeeId,
            @RequestParam String reason) {
        employeeService.blockEmployee(employeeId, reason);
        return ResponseEntity.ok("Empleado bloqueado con éxito.");
    }

}
