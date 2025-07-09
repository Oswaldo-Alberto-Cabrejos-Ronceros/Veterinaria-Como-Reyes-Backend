package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.WeeklyIncomeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/panel-manager")
public class ManagerController {

    private final ISpecieService specieService;
    private final IPaymentService paymentService;

    public ManagerController(ISpecieService specieService, IPaymentService paymentService) {
        this.specieService = specieService;
        this.paymentService = paymentService;
    }

    @GetMapping("/appointments/top-species/{headquarterId}")
    public ResponseEntity<TopSpeciesByAppointmentsDTO> getTopSpeciesByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(specieService.getTopSpeciesByHeadquarter(headquarterId));
    }

    @GetMapping("/payment/weekly/{headquarterId}")
    public ResponseEntity<WeeklyIncomeDTO> getWeeklyIncomeByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(paymentService.getWeeklyIncomeByHeadquarter(headquarterId));
    }
}
