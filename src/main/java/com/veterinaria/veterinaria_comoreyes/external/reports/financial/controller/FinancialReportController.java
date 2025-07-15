package com.veterinaria.veterinaria_comoreyes.external.reports.financial.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.exception.ReportGenerationException;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByHeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByPeriodDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.enums.ReportPeriod;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.service.FinancialReportService;
import com.veterinaria.veterinaria_comoreyes.external.reports.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reports/financial")
@RequiredArgsConstructor
@Slf4j
public class FinancialReportController {

    private final FinancialReportService financialReportService;
    private final SecurityUtil securityUtil;
    // NO
    @GetMapping("/income/{period}")
    public ResponseEntity<?> getIncomeReport(
            @PathVariable ReportPeriod period,
            @RequestParam(defaultValue = "pdf") String format) {

        try {
            List<IncomeByPeriodDTO> data = financialReportService.getIncomeByPeriod(period);

            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            if ("excel".equalsIgnoreCase(format)) {
                byte[] excel = financialReportService.generateIncomeByPeriodExcel(data, period);
                return buildExcelResponse(excel, period);
            } else {
                byte[] pdf = financialReportService.generateIncomeByPeriodPdf(data, period);
                return buildPdfResponse(pdf, period);
            }
        } catch (ReportGenerationException e) {
            log.error("Error al generar el reporte PDF/Excel: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Error al generar el reporte: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            log.error("Error inesperado: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Error interno del servidor: " + e.getMessage()).getBytes());
        }
    }
    // NO

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdf, ReportPeriod period) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition",
                        "inline; filename=ingresos_" + period.name().toLowerCase() + ".pdf")
                .body(pdf);
    }
    // NO

    private ResponseEntity<byte[]> buildExcelResponse(byte[] excel, ReportPeriod period) {
        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header("Content-Disposition",
                        "attachment; filename=ingresos_" + period.name().toLowerCase() + ".xlsx")
                .body(excel);
    }


    // NO
    @GetMapping("/income/by-service")
    public ResponseEntity<byte[]> getIncomeByServicePdf() {
        try {
            var data = financialReportService.getIncomeByService();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            byte[] pdf = financialReportService.generateIncomeByServicePdf(data);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "inline; filename=ingresos_por_servicio.pdf")
                    .body(pdf);
        } catch (Exception e) {
            log.error("Error al generar PDF ingresos por servicio", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // NO
    @GetMapping("/income/by-specie")
    public ResponseEntity<byte[]> getIncomeBySpeciePdf() {
        try {
            var data = financialReportService.getIncomeBySpecie();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            byte[] pdf = financialReportService.generateIncomeBySpeciePdf(data);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "inline; filename=ingresos_por_especie.pdf")
                    .body(pdf);
        } catch (Exception e) {
            log.error("Error al generar PDF ingresos por especie", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // NO
    @GetMapping("/payment-method")
    public ResponseEntity<byte[]> getPaymentMethodExcel() {
        try {
            var data = financialReportService.getIncomeByPaymentMethod();

            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            byte[] excel = financialReportService.generatePaymentMethodExcel(data);
            return ResponseEntity.ok()
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header("Content-Disposition", "attachment; filename=metodos_pago.xlsx")
                    .body(excel);
        } catch (Exception e) {
            log.error("Error al generar Excel de métodos de pago", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    // ESTE SE USARÁ GET http://localhost:8080/api/reports/financial/income/by-period-service/DAILY -> MONTHLY, YEARLY
    @GetMapping("/income/by-period-service/{period}")
    public ResponseEntity<byte[]> getIncomeByPeriodAndServicePdf(@PathVariable ReportPeriod period) {
        try {
            var data = financialReportService.getIncomeByPeriodAndService(period);

            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // 👤 Empleado autenticado (autor)
            Employee empleado = securityUtil.getAuthenticatedEmployee();
            String autor = empleado.getName() + " " + empleado.getLastName() + " - " +
                    (empleado.getRoles().isEmpty() ? "Empleado" : empleado.getRoles().get(0).getName());

            // 🗓️ Fecha de generación (ej. 13 de julio, 2025)
            String fechaGeneracion = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", new Locale("es", "PE")));

            String empresa = "Veterinaria Como Reyes";

            byte[] pdf = financialReportService.generateIncomeByPeriodAndServicePdf(
                    data, period, autor, fechaGeneracion, empresa);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition",
                            "inline; filename=ingresos_por_periodo_servicio_" + period.name().toLowerCase() + ".pdf")
                    .body(pdf);

        } catch (Exception e) {
            log.error("Error al generar PDF ingresos por servicio y periodo", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // @GetMapping("/income/by-headquarter/pdf")
    // public ResponseEntity<byte[]> getIncomeReportByHeadquarterPdf() {
    // try {
    // List<IncomeByHeadquarterDTO> report =
    // financialReportService.getIncomeReportByHeadquarter();

    // if (report == null || report.isEmpty()) {
    // return ResponseEntity.noContent().build();
    // }

    // byte[] pdf = financialReportService.generateIncomeByHeadquarterPdf(report);

    // return ResponseEntity.ok()
    // .contentType(MediaType.APPLICATION_PDF)
    // .header("Content-Disposition", "inline; filename=ingresos_por_sede.pdf")
    // .body(pdf);
    // } catch (Exception e) {
    // log.error("Error al generar PDF de ingresos por sede", e);
    // return ResponseEntity.internalServerError().build();
    // }
    // }

    // ESTE SE USARÁ GET http://localhost:8080/api/reports/financial/income/by-headquarter/period/pdf?period=DAILY-MONTHLY-YEARLY
    @GetMapping("/income/by-headquarter/period/pdf")
    public ResponseEntity<byte[]> getIncomeByHeadquarterPeriodPdf(
            @RequestParam(defaultValue = "monthly") ReportPeriod period) {
        try {
            List<IncomeByHeadquarterDTO> report = financialReportService.getIncomeReportByHeadquarter(period);

            if (report == null || report.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Empleado autenticado
            Employee empleado = securityUtil.getAuthenticatedEmployee();
            String autor = empleado.getName() + " " + empleado.getLastName() + " - " +
                    (empleado.getRoles().isEmpty() ? "Empleado" : empleado.getRoles().get(0).getName());

            String fechaGeneracion = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", new Locale("es", "PE")));

            String empresa = "Veterinaria Como Reyes";

            byte[] pdf = financialReportService.generateIncomeByHeadquarterPdf(
                    report, period, autor, fechaGeneracion, empresa);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition",
                            "inline; filename=ingresos_por_sede_" + period.name().toLowerCase() + ".pdf")
                    .body(pdf);
        } catch (Exception e) {
            log.error("Error al generar PDF de ingresos por sede y periodo", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
