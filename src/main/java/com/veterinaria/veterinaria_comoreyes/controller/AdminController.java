package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/panel-admin")
public class AdminController {

    private final ISpecieService specieService;

    public AdminController(ISpecieService specieService) {
        this.specieService = specieService;
    }

    @GetMapping("/appointments/top-species")
    public ResponseEntity<TopSpeciesByAppointmentsDTO> getTopSpeciesGeneral() {
        return ResponseEntity.ok(specieService.getTopSpeciesGeneral());
    }
}
