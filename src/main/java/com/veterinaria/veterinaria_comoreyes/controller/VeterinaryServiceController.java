package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Service.ServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.ServicesInfoTopPanelAdminDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;
import com.veterinaria.veterinaria_comoreyes.service.IVeterinaryServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PutMapping("/{serviceId}/activate")
    public ResponseEntity<Void> activateVeterinaryService(@PathVariable Long serviceId) {
        veterinaryServiceService.activateVeterinaryService(serviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public Page<ServiceListDTO> searchVeterinaryServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specie,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean status,
            @PageableDefault(size = 10) Pageable pageable) {
        return veterinaryServiceService.searchServices(name, specie, category, status, pageable);
    }

    // Panel admin: todos los servicios más solicitados
    @GetMapping("/panel-admin/top-services")
    public List<ServicesInfoTopPanelAdminDTO> getTopServicesForAdmin() {
        return veterinaryServiceService.getTopServicesPanelAdmin();
    }

    // Panel encargado de sede: filtrado por sede
    @GetMapping("/panel-manager/top-services/{headquarterId}")
    public List<ServicesInfoTopPanelAdminDTO> getTopServicesForManager(@PathVariable Long headquarterId) {
        return veterinaryServiceService.getTopServicesPanelManager(headquarterId);
    }
}
