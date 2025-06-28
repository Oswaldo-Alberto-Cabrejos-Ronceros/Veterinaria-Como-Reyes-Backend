package com.veterinaria.veterinaria_comoreyes.controller;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterEmployeesDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterListDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/headquarters")
public class HeadquarterController {

    @Autowired
    private IHeadquarterService headquarterService;

    @GetMapping
    public List<HeadquarterDTO> getAllHeadquarters() {
        return headquarterService.getAllHeadquarters();
    }

    @GetMapping("/{id}")
    public HeadquarterDTO getHeadquarterById(@PathVariable Long id) {
        return headquarterService.getHeadquarterById(id);
    }

    @PostMapping
    public HeadquarterDTO createHeadquarter(@Valid @RequestBody HeadquarterDTO dto) {
        return headquarterService.createHeadquarter(dto);
    }

    @PutMapping("/{id}")
    public HeadquarterDTO updateHeadquarter(@PathVariable Long id, @Valid @RequestBody HeadquarterDTO dto) {
        return headquarterService.updateHeadquarter(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteHeadquarter(@PathVariable Long id) {
        headquarterService.deleteHeadquarter(id);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<HeadquarterEmployeesDTO>> getAllActiveHeadquartersWithActiveEmployees() {
        List<HeadquarterEmployeesDTO> dtos = headquarterService.getAllActiveHeadquartersWithActiveEmployees();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HeadquarterListDTO>> searchHeadquarters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<HeadquarterListDTO> result = headquarterService.searcHeadquarters(name, address, district, status,
                pageable);
        return ResponseEntity.ok(result);
    }

}
