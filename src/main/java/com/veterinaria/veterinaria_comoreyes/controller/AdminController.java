package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.WeeklyIncomeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel-admin")
public class AdminController {

    private final ISpecieService specieService;
    private final IPaymentService paymentService;

    public AdminController(ISpecieService specieService, IPaymentService paymentService) {
        this.specieService = specieService;
        this.paymentService = paymentService;
    }

    @GetMapping("/appointments/top-species")
    public ResponseEntity<TopSpeciesByAppointmentsDTO> getTopSpeciesGeneral() {
        return ResponseEntity.ok(specieService.getTopSpeciesGeneral());
    }

    @GetMapping("/payment/weekly")
    public ResponseEntity<WeeklyIncomeDTO> getWeeklyIncomeGeneral() {
        return ResponseEntity.ok(paymentService.getWeeklyIncomeGeneral());
    }


}
