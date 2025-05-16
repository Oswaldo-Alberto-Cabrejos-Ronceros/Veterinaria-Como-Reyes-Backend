package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.EmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.EmployeeListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.MyInfoClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.MyInfoEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.mapper.EmployeeMapper;
import com.veterinaria.veterinaria_comoreyes.service.IEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    //Search employee for id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    //New employee with user optional
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employee) {
        EmployeeDTO employeeCreated = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeCreated);
    }

    //Update complete employee data
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employee) {
        EmployeeDTO employeeUpdated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(employeeUpdated);
    }

    // changes status to block
    @PatchMapping("/{id}/block")
    public ResponseEntity<?> blockEmployee(@PathVariable Long id) {
        employeeService.blockEmployee(id);
        return ResponseEntity.noContent().build();
    }

    //changes status to active
    @PatchMapping("/{id}/restore")
    public ResponseEntity<?> restoreEmployee(@PathVariable Long id) {
        employeeService.restoreEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }*/

    // Listing and searching employees for the table with pagination
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeListDTO>> searchEmployees(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Byte status,
            @RequestParam(required = false) Long headquarterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeListDTO> result = employeeService.searchEmployees(dni, name, lastName, status, headquarterId, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/myInfo")
    public ResponseEntity<MyInfoEmployeeDTO> getEmployeeMyInfo(@PathVariable Long id,
                                                               @CookieValue("accessToken") String token) {
        MyInfoEmployeeDTO myInfoEmployee = employeeService.myInfoAsEmployee(token,id);
        return ResponseEntity.ok(myInfoEmployee);
    }


}
