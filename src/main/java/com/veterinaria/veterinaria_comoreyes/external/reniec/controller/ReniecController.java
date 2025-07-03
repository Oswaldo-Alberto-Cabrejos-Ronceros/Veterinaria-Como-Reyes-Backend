package com.veterinaria.veterinaria_comoreyes.external.reniec.controller;

import com.veterinaria.veterinaria_comoreyes.external.reniec.dto.ReniecResponseDTO;
import com.veterinaria.veterinaria_comoreyes.external.reniec.dto.ReniecResponseSimpleDTO;
import com.veterinaria.veterinaria_comoreyes.external.reniec.service.IReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reniec")
public class ReniecController {


    private final IReniecService reniecService;

    @Autowired
    public ReniecController(IReniecService reniecService) {
        this.reniecService = reniecService;
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ReniecResponseDTO> consultPorDni(
            @PathVariable("dni") String dni
    ) {
        ReniecResponseDTO datos = reniecService.consultDni(dni);
        return ResponseEntity.ok(datos);
    }
    @GetMapping("/{dni}/simple")
    public ResponseEntity<ReniecResponseSimpleDTO> getReniecData(@PathVariable String dni) {
        ReniecResponseSimpleDTO result = reniecService.consultDniSimple(dni);
        return ResponseEntity.ok(result);
    }
}
