package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Service.VetServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;
import com.veterinaria.veterinaria_comoreyes.service.IVeterinaryServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinary-services")
public class VeterinaryServiceController {
    private final IVeterinaryServiceService veterinaryServiceService;

    @Autowired
    public VeterinaryServiceController(IVeterinaryServiceService veterinaryServiceService) {
        this.veterinaryServiceService = veterinaryServiceService;
    }

    @GetMapping
    public ResponseEntity<List<VeterinaryServiceDTO>> getVeterinaryServices() {
        return ResponseEntity.ok(veterinaryServiceService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinaryServiceDTO> getVeterinaryServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(veterinaryServiceService.getServiceById(id));
    }

    @GetMapping("/specie/{id}")
    public ResponseEntity<VeterinaryServiceDTO> getVeterinaryServiceBySpecie(@PathVariable Long id) {
        return ResponseEntity.ok(veterinaryServiceService.getServiceById(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<VeterinaryServiceDTO> getVeterinaryServiceByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(veterinaryServiceService.getServiceById(id));
    }

    @PostMapping
    public ResponseEntity<VeterinaryServiceDTO> createVeterinaryService(
            @RequestBody VeterinaryServiceDTO veterinaryServiceDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(veterinaryServiceService.createService(veterinaryServiceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinaryServiceDTO> updateService(@PathVariable Long id,
            @RequestBody VeterinaryServiceDTO veterinaryServiceDTO) {
        return ResponseEntity.ok(veterinaryServiceService.updateService(id, veterinaryServiceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        veterinaryServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<VetServiceListDTO>> searchVeterinaryServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String specieId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VetServiceListDTO> result = veterinaryServiceService.searchVetServices(
                name, price, specieId, categoryId, status, pageable);
        return ResponseEntity.ok(result);
    }

}
