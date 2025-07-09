package com.veterinaria.veterinaria_comoreyes.external.reports.financial.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaria.veterinaria_comoreyes.exception.ReportGenerationException;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByPeriodDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.enums.ReportPeriod;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.service.FinancialReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports/financial")
@RequiredArgsConstructor
public class FinancialReportController {

    private final FinancialReportService financialReportService;

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
            return handleReportError(e);
        }
    }

    private ResponseEntity<byte[]> buildPdfResponse(byte[] pdf, ReportPeriod period) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition",
                        "inline; filename=ingresos_" + period.name().toLowerCase() + ".pdf")
                .body(pdf);
    }

    private ResponseEntity<byte[]> buildExcelResponse(byte[] excel, ReportPeriod period) {
        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header("Content-Disposition",
                        "attachment; filename=ingresos_" + period.name().toLowerCase() + ".xlsx")
                .body(excel);
    }

    private ResponseEntity<ErrorResponse> handleReportError(ReportGenerationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "REPORT_GENERATION_ERROR",
                        "Error al generar el reporte: " + e.getMessage(),
                        e.getCause() != null ? e.getCause().getMessage() : null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        "Error interno del servidor",
                        ex.getMessage()));
    }

    @RequiredArgsConstructor
    private static class ErrorResponse {
        private final String code;
        private final String message;
        private final String detail;
    }
}