package com.veterinaria.veterinaria_comoreyes.controller;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterEmployeesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;

import jakarta.validation.Valid;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @PutMapping("/{headquarterId}/activate")
    public ResponseEntity<Void> activateHeadquarter(@PathVariable Long headquarterId) {
        headquarterService.activateHeadquarter(headquarterId);
        return ResponseEntity.ok().build();
    }
}
