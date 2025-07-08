package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeBasicInfoDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service.HeadquarterVetServiceDTO;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterVetServiceService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/headquarter-vet-services")
@RequiredArgsConstructor
public class HeadquarterVetServiceController {

    @Autowired
    private final IHeadquarterVetServiceService headquarterVetServiceService;

    // Obtener todos
    @GetMapping
    public ResponseEntity<List<HeadquarterVetServiceDTO>> getAll() {
        return ResponseEntity.ok(headquarterVetServiceService.callAllHeadquarterVetServiceByHeadquarterId());
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<HeadquarterVetServiceDTO> getById(@PathVariable Long id) {
        Optional<HeadquarterVetServiceDTO> result = headquarterVetServiceService.getHeadquarterVetServiceById(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Obtener por ID de sede
    @GetMapping("/headquarter/{headquarterId}")
    public ResponseEntity<List<HeadquarterVetServiceDTO>> getByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(headquarterVetServiceService.callHeadquarterVetServiceByHeadquarter(headquarterId));
    }

    // Crear relación sede-servicio
    @PostMapping
    public ResponseEntity<HeadquarterVetServiceDTO> create(@RequestBody HeadquarterVetServiceDTO dto) {
        return ResponseEntity.ok(headquarterVetServiceService.createHeadquarterVetService(dto));
    }

    // Actualizar relación
    @PutMapping("/{id}")
    public ResponseEntity<HeadquarterVetServiceDTO> update(@PathVariable Long id, @RequestBody HeadquarterVetServiceDTO dto) {
        return ResponseEntity.ok(headquarterVetServiceService.updateHeadquarterVetService(id, dto));
    }

    // Eliminar (lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        headquarterVetServiceService.deleteHeadquarterVetService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/veterinarians")
    public ResponseEntity<List<EmployeeBasicInfoDTO>> listVeterinariansByHvs(
            @RequestParam Long headquarterVetServiceId
    ) {
        List<EmployeeBasicInfoDTO> vets = headquarterVetServiceService.getVeterinariansByHvs(headquarterVetServiceId);
        if (vets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vets);
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> enable(@PathVariable Long id) {
        headquarterVetServiceService.enableHeadquarterVetService(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/capacity")
    public ResponseEntity<Void> updateCapacity(@PathVariable Long id,
                                               @RequestBody Map<String, Integer> body) {
        Integer capacity = body.get("simultaneousCapacity");
        headquarterVetServiceService.updateSimultaneousCapacity(id, capacity);
        return ResponseEntity.noContent().build();
    }

}
