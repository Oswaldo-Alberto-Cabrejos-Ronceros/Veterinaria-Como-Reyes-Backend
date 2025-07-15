package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.DailyAppointmentStatsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.MonthlyStatsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.OperationalMonthlyStatsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.AnnualRevenueDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.IncomePerHeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.TopPaymentMethodsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.WeeklyIncomeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesCareDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import com.veterinaria.veterinaria_comoreyes.service.ICareService;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel-admin")
public class AdminController {

    private final ISpecieService specieService;
    private final IPaymentService paymentService;
    private final IAppointmentService appointmentService;
    private final ICareService careService;

    public AdminController(ISpecieService specieService, IPaymentService paymentService, IAppointmentService appointmentService, ICareService careService) {
        this.specieService = specieService;
        this.paymentService = paymentService;
        this.appointmentService = appointmentService;
        this.careService = careService;
    }

    @GetMapping("/appointments/top-species")
    public ResponseEntity<TopSpeciesByAppointmentsDTO> getTopSpeciesGeneral() {
        return ResponseEntity.ok(specieService.getTopSpeciesGeneral());
    }

    @GetMapping("/payment/weekly")
    public ResponseEntity<WeeklyIncomeDTO> getWeeklyIncomeGeneral() {
        return ResponseEntity.ok(paymentService.getWeeklyIncomeGeneral());
    }

    /*GRAFICO PASTEL */
    @GetMapping("/payment-method/{period}") //PERIOD: "WEEK", "MONTH", "YEAR"
    public ResponseEntity<TopPaymentMethodsDTO> getTopPaymentMethods(String period) {
        return ResponseEntity.ok(paymentService.getTopPaymentMethods(period));
    }

    /*GRAFICO xx */
    @GetMapping("/top-specie/{period}")
    public ResponseEntity<TopSpeciesCareDTO> getTopSpeciesByPeriod(@PathVariable String period) {
        return ResponseEntity.ok(specieService.getTopSpeciesByPeriod(period));
    }

    /*GRAFICO BARRAS */
    @GetMapping("/income-by-headquarter/{period}")
    public ResponseEntity<IncomePerHeadquarterDTO> getIncomePerHeadquarter(@PathVariable String period) {
        IncomePerHeadquarterDTO dto = paymentService.getIncomePerHeadquarterByPeriod(period);
        return ResponseEntity.ok(dto);
    }
    /*GRAFICO xxx */
    @GetMapping("/annual")
    public ResponseEntity<AnnualRevenueDTO> getAnnualEvolution() {
        return ResponseEntity.ok(paymentService.getAnnualFinancialEvolution());
    }
    /*ESTADISTICAS MENSUAL finaciera */
    @GetMapping("/monthly-stats/general")
    public ResponseEntity<MonthlyStatsDTO> getGeneralMonthlyStats() {
        return ResponseEntity.ok(appointmentService.getGeneralMonthlyStats());
    }

    /*ESTADISTICAS MENSUAL operacionales*/

    @GetMapping("/monthly-stats/operational/general")
    public ResponseEntity<OperationalMonthlyStatsDTO> getGeneralOperationalMonthlyStats() {
        return ResponseEntity.ok(careService.getGeneralOperationalMonthlyStats());
    }

    //grafico de citas completadas y canceladas por dia de la semana
    @GetMapping("/appointments/daily-stats")
    public ResponseEntity<DailyAppointmentStatsDTO> getDailyAppointmentStatsLast7Days() {
        return ResponseEntity.ok(appointmentService.getDailyAppointmentStatsLast7Days());
    }











}
