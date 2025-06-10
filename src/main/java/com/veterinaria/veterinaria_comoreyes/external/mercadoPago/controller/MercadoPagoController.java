package com.veterinaria.veterinaria_comoreyes.external.mercadoPago.controller;

import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.service.IMerPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mp")
public class MercadoPagoController {

    private final IMerPagoService mercadoPagoService;

    @Autowired
    public MercadoPagoController(IMerPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping
    public ResponseEntity<String> createPreference(@RequestBody UserBuyerDTO userBuyerDTO) {
        try {
            String initPoint = mercadoPagoService.createPreference(userBuyerDTO);
            return ResponseEntity.ok(initPoint);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    // Crea preferencia de pago a partir de Appointment ID
    @PostMapping("/appointment/{idAppointment}")
    public ResponseEntity<String> createPreferenceByAppointment(@PathVariable Long idAppointment) {
        String preferenceUrl = mercadoPagoService.createPreferenceByIdAppointment(idAppointment);
        return ResponseEntity.ok(preferenceUrl);
    }

    // Crea preferencia de pago a partir de Care ID
    @PostMapping("/care/{idCare}")
    public ResponseEntity<String> createPreferenceByCare(@PathVariable Long idCare) {
        String preferenceUrl = mercadoPagoService.createPreferenceByIdCare(idCare);
        return ResponseEntity.ok(preferenceUrl);
    }
}
