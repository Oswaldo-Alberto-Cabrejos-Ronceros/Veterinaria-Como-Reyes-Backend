package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordDTO;
import com.veterinaria.veterinaria_comoreyes.entity.StatusVeterinaryRecord;
import com.veterinaria.veterinaria_comoreyes.service.IVeterinaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinary-records")
@RequiredArgsConstructor
public class VeterinaryRecordController {
    private final IVeterinaryRecordService service;

    @PostMapping
    public ResponseEntity<VeterinaryRecordDTO> create(@RequestBody VeterinaryRecordDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<VeterinaryRecordDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinaryRecordDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinaryRecordDTO> update(@PathVariable Long id, @RequestBody VeterinaryRecordDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // Cambiar estado a EN_CURSO
    @PatchMapping("/{id}/status/curso")
    public ResponseEntity<VeterinaryRecordDTO> setEnCurso(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateStatus(id, StatusVeterinaryRecord.EN_CURSO));
    }

    // Cambiar estado a COMPLETADO
    @PatchMapping("/{id}/status/completado")
    public ResponseEntity<VeterinaryRecordDTO> setCompletado(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateStatus(id, StatusVeterinaryRecord.COMPLETADO));
    }

    // Cambiar estado a OBSERVACION
    @PatchMapping("/{id}/status/observacion")
    public ResponseEntity<VeterinaryRecordDTO> setObservacion(@PathVariable Long id) {
        return ResponseEntity.ok(service.updateStatus(id, StatusVeterinaryRecord.OBSERVACION));
    }
}
