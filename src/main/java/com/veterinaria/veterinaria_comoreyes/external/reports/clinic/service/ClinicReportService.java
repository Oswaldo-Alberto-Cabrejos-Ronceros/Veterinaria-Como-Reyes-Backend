package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.service;

import com.veterinaria.veterinaria_comoreyes.exception.ReportGenerationException;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.*;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.repository.ClinicReportRepository;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.enums.ReportPeriod;
import com.veterinaria.veterinaria_comoreyes.external.reports.utils.ExcelUtils;
import com.veterinaria.veterinaria_comoreyes.external.reports.utils.PdfGenerator;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClinicReportService {

    private final ClinicReportRepository clinicReportRepository;
    private final PdfGenerator pdfGenerator;

    // === 1. Citas por periodo ===

    public List<AppointmentsByTimeDTO> getAppointmentsByPeriod(ReportPeriod period) {
        List<Object[]> rows = switch (period) {
            case DAILY -> clinicReportRepository.findDailyAppointments();
            case WEEKLY -> clinicReportRepository.findWeeklyAppointments();
            case MONTHLY -> clinicReportRepository.findMonthlyAppointments();
            case YEARLY -> clinicReportRepository.findYearlyAppointments();
        };

        return rows.stream()
                .map(r -> new AppointmentsByTimeDTO((String) r[0], (Number) r[1]))
                .toList();
    }

    public byte[] generateAppointmentsByPeriodPdf(ReportPeriod period) {
        List<AppointmentsByTimeDTO> data = getAppointmentsByPeriod(period);
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Reporte de Citas por " + period.getDisplayName());

            long total = data.stream().mapToLong(AppointmentsByTimeDTO::getCount).sum();
            model.put("total", total);

            return pdfGenerator.generatePdf("reports/clinic/appointments-by-period", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar PDF de citas por periodo", e);
        }
    }

    public byte[] generateAppointmentsByPeriodExcel(ReportPeriod period) {
        List<AppointmentsByTimeDTO> data = getAppointmentsByPeriod(period);
        try (Workbook workbook = new XSSFWorkbook()) {
            String[] headers = { "Periodo", "Cantidad de Citas" };
            String sheetName = "Citas " + period.getDisplayName();
            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (AppointmentsByTimeDTO item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getPeriod());
                row.createCell(1).setCellValue(item.getCount());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return ExcelUtils.workbookToBytes(workbook);
        } catch (Exception e) {
            throw new ReportGenerationException("Error al generar Excel de citas por periodo", e);
        }
    }

    private String getFormatPattern(ReportPeriod period) {
        return switch (period) {
            case DAILY -> "YYYY-MM-DD";
            case WEEKLY -> "YYYY-IW"; // Semana ISO
            case MONTHLY -> "YYYY-MM";
            case YEARLY -> "YYYY";
        };
    }

    // === 2. Citas por veterinario ===
    public List<AppointmentsByVetDTO> getAppointmentsByVet() {
        return clinicReportRepository.findAppointmentsByVet();
    }

    public byte[] generateAppointmentsByVetPdf() {
        List<AppointmentsByVetDTO> data = getAppointmentsByVet();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Citas por Médico Veterinario");

            long total = data.stream().mapToLong(AppointmentsByVetDTO::getTotalAppointments).sum();
            model.put("total", total);

            return pdfGenerator.generatePdf("reports/clinic/appointments-by-vet", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar PDF de citas por veterinario", e);
        }
    }

    // === 3. Servicios más solicitados ===
    public List<PopularServicesDTO> getPopularServices() {
        return clinicReportRepository.findPopularServices();
    }

    public byte[] generatePopularServicesPdf() {
        List<PopularServicesDTO> data = getPopularServices();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Servicios más solicitados");

            long total = data.stream().mapToLong(PopularServicesDTO::getCount).sum();
            model.put("total", total);

            return pdfGenerator.generatePdf("reports/clinic/popular-services", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar PDF de servicios populares", e);
        }
    }

    // === 4. Animales más atendidos ===
    public List<AnimalsByTypeDTO> getAnimalsBySpecieOrBreed() {
        return clinicReportRepository.findAnimalsBySpecieOrBreed();
    }

    public byte[] generateAnimalsBySpecieOrBreedPdf() {
        List<AnimalsByTypeDTO> data = getAnimalsBySpecieOrBreed();
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Animales más Atendidos (Especie/Raza)");

            long total = data.stream().mapToLong(AnimalsByTypeDTO::getCount).sum();
            model.put("total", total);

            return pdfGenerator.generatePdf("reports/clinic/animals-by-type", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar PDF de animales por especie/raza", e);
        }
    }

    public List<AppointmentsByVetAndPeriodDTO> getAppointmentsByVetAndPeriod(ReportPeriod period) {
        String pattern = switch (period) {
            case DAILY -> "YYYY-MM-DD";
            case WEEKLY -> "YYYY-IW";
            case MONTHLY -> "MM/YYYY";
            case YEARLY -> "YYYY";
        };
        return clinicReportRepository.findAppointmentsByVetAndPeriod(pattern);
    }

    public byte[] generateAppointmentsByVetAndPeriodPdf(ReportPeriod period) {
        List<AppointmentsByVetAndPeriodDTO> data = getAppointmentsByVetAndPeriod(period);
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Citas por Veterinario y " + period.getDisplayName());

            long total = data.stream().mapToLong(AppointmentsByVetAndPeriodDTO::getCompletedAppointments).sum();
            model.put("total", total);

            return pdfGenerator.generatePdf("reports/clinic/appointments-by-vet-and-period", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar PDF de citas por veterinario y periodo", e);
        }
    }

    public byte[] generateAppointmentsByVetAndPeriodExcel(ReportPeriod period) {
        List<AppointmentsByVetAndPeriodDTO> data = getAppointmentsByVetAndPeriod(period);
        try (Workbook workbook = new XSSFWorkbook()) {
            String[] headers = { "Veterinario", "Periodo", "Citas Completadas" };
            String sheetName = "Vet " + period.getDisplayName();
            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (AppointmentsByVetAndPeriodDTO item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getVetName());
                row.createCell(1).setCellValue(item.getPeriod());
                row.createCell(2).setCellValue(item.getCompletedAppointments());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return ExcelUtils.workbookToBytes(workbook);
        } catch (Exception e) {
            throw new ReportGenerationException("Error al generar Excel de citas por veterinario y periodo", e);
        }
    }

}
