package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.DailyAppointmentStatsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.MonthlyStatsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.OperationalMonthlyStatsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.VeterinarianPerformanceDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.AnnualRevenueDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.TopPaymentMethodsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.WeeklyIncomeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesCareDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import com.veterinaria.veterinaria_comoreyes.service.ICareService;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/panel-manager")
public class ManagerController {

    private final ISpecieService specieService;
    private final IPaymentService paymentService;
    private final IAppointmentService appointmentService;
    private final ICareService careService;

    public ManagerController(ISpecieService specieService, IPaymentService paymentService, IAppointmentService appointmentService, ICareService careService) {
        this.specieService = specieService;
        this.paymentService = paymentService;
        this.appointmentService = appointmentService;
        this.careService = careService;
    }

    @GetMapping("/appointments/top-species/{headquarterId}")
    public ResponseEntity<TopSpeciesByAppointmentsDTO> getTopSpeciesByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(specieService.getTopSpeciesByHeadquarter(headquarterId));
    }

    @GetMapping("/payment/weekly/{headquarterId}")
    public ResponseEntity<WeeklyIncomeDTO> getWeeklyIncomeByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(paymentService.getWeeklyIncomeByHeadquarter(headquarterId));
    }

    // GRAFICOS PASTER
    @GetMapping("/payment-method/{period}/headquarter/{headquarterId}") //PERIOD: "WEEK", "MONTH", "YEAR"
    public ResponseEntity<TopPaymentMethodsDTO> getTopPaymentMethodsByHeadquarter(
            @PathVariable String period,
            @PathVariable Long headquarterId
    ) {
        TopPaymentMethodsDTO dto = paymentService.getTopPaymentMethodsByHeadquarter(period, headquarterId);
        return ResponseEntity.ok(dto);
    }

    // GRAFICO xx
    @GetMapping("/top-specie/{period}/headquarter/{headquarterId}")
    public ResponseEntity<TopSpeciesCareDTO> getTopSpeciesByPeriodAndHeadquarter(
            @PathVariable String period,
            @PathVariable Long headquarterId) {
        return ResponseEntity.ok(specieService.getTopSpeciesByPeriodAndHeadquarter(period, headquarterId));
    }

    // GRAFICO ANNUAL
    @GetMapping("/annual/headquarter/{headquarterId}")
    public ResponseEntity<AnnualRevenueDTO> getAnnualEvolutionByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(paymentService.getAnnualFinancialEvolutionByHeadquarter(headquarterId));
    }

    //estadisticas finacieras mensuales por sede
    @GetMapping("/monthly/by-headquarter/{headquarterId}")
    public ResponseEntity<MonthlyStatsDTO> getMonthlyStatsByHeadquarter(@PathVariable Long headquarterId) {
        MonthlyStatsDTO stats = appointmentService.getMonthlyStatsByHeadquarter(headquarterId);
        return ResponseEntity.ok(stats);
    }
    //estadisticas operacionales mensuales por sede
    @GetMapping("/monthly/operational/by-headquarter/{headquarterId}")
    public ResponseEntity<OperationalMonthlyStatsDTO> getOperationalStatsByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(careService.getOperationalMonthlyStatsByHeadquarter(headquarterId));
    }
    //graficos de barras de citas diarias por sede
    @GetMapping("/appointments/daily-stats/headquarter/{headquarterId}")
    public ResponseEntity<DailyAppointmentStatsDTO> getDailyAppointmentStatsByHeadquarter(
            @PathVariable Long headquarterId) {
        return ResponseEntity.ok(appointmentService.getDailyAppointmentStatsLast7DaysByHeadquarter(headquarterId));
    }

    //grafico de veterinarios por sede performance
    @GetMapping("/top-veterinarians/{period}/headquarter/{headquarterId}")
    public VeterinarianPerformanceDTO getTopByHeadquarter(@PathVariable String period, @PathVariable Long headquarterId) {
        return careService.getTopPerformanceByHeadquarter(period, headquarterId);
    }

}
