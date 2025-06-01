package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Care.CareDTO;
import com.veterinaria.veterinaria_comoreyes.service.ICareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cares")
@RequiredArgsConstructor
public class CareController {

    private final ICareService careService;

    // Obtener todas las atenciones
    @GetMapping
    public ResponseEntity<List<CareDTO>> getAllCares() {
        return ResponseEntity.ok(careService.getAllCares());
    }

    // Obtener atención por ID
    @GetMapping("/{id}")
    public ResponseEntity<CareDTO> getCareById(@PathVariable Long id) {
        return ResponseEntity.ok(careService.getCareById(id));
    }

    // Obtener atenciones por cita
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<CareDTO>> getCareByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(careService.getCareByAppointment(appointmentId));
    }

    // Crear atención (statusCare se define como EN_CURSO automáticamente)
    @PostMapping
    public ResponseEntity<CareDTO> createCare(@RequestBody CareDTO careDTO) {
        return ResponseEntity.ok(careService.createCare(careDTO));
    }

    // Marcar atención como COMPLETADO
    @PatchMapping("/{id}/complete")
    public ResponseEntity<CareDTO> completeCare(@PathVariable Long id) {
        return ResponseEntity.ok(careService.completeCare(id));
    }

    // Actualizar atención (completo)
    @PutMapping("/{id}")
    public ResponseEntity<CareDTO> updateCare(@PathVariable Long id, @RequestBody CareDTO careDTO) {
        return ResponseEntity.ok(careService.updateCare(id, careDTO));
    }

    // Eliminar atención
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteCare(@PathVariable Long id) {
    //     careService.deleteCare(id);
    //     return ResponseEntity.noContent().build();
    // }
}
