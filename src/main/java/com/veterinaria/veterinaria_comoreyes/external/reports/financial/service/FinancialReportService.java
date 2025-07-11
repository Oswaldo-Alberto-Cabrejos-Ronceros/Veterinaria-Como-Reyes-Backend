package com.veterinaria.veterinaria_comoreyes.external.reports.financial.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import com.veterinaria.veterinaria_comoreyes.exception.ReportGenerationException;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.*;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.enums.ReportPeriod;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.repository.FinancialReportRepository;
import com.veterinaria.veterinaria_comoreyes.external.reports.utils.ExcelUtils;
import com.veterinaria.veterinaria_comoreyes.external.reports.utils.PdfGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinancialReportService {

    private final FinancialReportRepository financialReportRepository;
    private final PdfGenerator pdfGenerator;

    // ========= Ingresos por Periodo =========

    public List<IncomeByPeriodDTO> getIncomeByPeriod(ReportPeriod period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = getStartDateByPeriod(period);
        String pattern = getDatePattern(period);
        return financialReportRepository.findIncomeByPeriod(startDate, endDate, pattern);
    }

    public byte[] generateIncomeByPeriodPdf(List<IncomeByPeriodDTO> data, ReportPeriod period) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Reporte de Ingresos");
            model.put("period", period.getDisplayName());

            double total = data.stream().mapToDouble(IncomeByPeriodDTO::getTotal).sum();
            model.put("total", String.format("S/%,.2f", total));

            String base64Chart = generateChartAsBase64(data);
            model.put("chartImage", base64Chart);

            return pdfGenerator.generatePdf("reports/financial/income-by-period", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar el reporte PDF", e);
        }
    }

    public byte[] generateIncomeByPeriodExcel(List<IncomeByPeriodDTO> data, ReportPeriod period) {
        try (Workbook workbook = new XSSFWorkbook()) {
            String[] headers = { "Periodo", "Total (S/.)" };
            Sheet sheet = workbook.createSheet("Ingresos " + period.getDisplayName());

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("S/#,##0.00"));

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (IncomeByPeriodDTO item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getPeriod());

                Cell amountCell = row.createCell(1);
                amountCell.setCellValue(item.getTotal());
                amountCell.setCellStyle(currencyStyle);
            }

            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(0).setCellValue("TOTAL");

            Cell totalCell = totalRow.createCell(1);
            totalCell.setCellFormula(String.format("SUM(B2:B%d)", rowNum));
            totalCell.setCellStyle(currencyStyle);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return ExcelUtils.workbookToBytes(workbook);
        } catch (Exception e) {
            throw new ReportGenerationException("Error al generar el reporte Excel: " + e.getMessage(), e);
        }
    }

    private String getDatePattern(ReportPeriod period) {
        return switch (period) {
            case DAILY -> "DD/MM/YYYY";
            case WEEKLY -> "YYYY/IW";
            case MONTHLY -> "MM/YYYY";
            case YEARLY -> "YYYY";
        };
    }

    private LocalDateTime getStartDateByPeriod(ReportPeriod period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period) {
            case DAILY -> now.withHour(0).withMinute(0).withSecond(0).withNano(0);
            case WEEKLY -> now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case MONTHLY -> now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case YEARLY -> now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        };
    }

    private LocalDateTime getEndDateByPeriod(ReportPeriod period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period) {
            case DAILY -> now.withHour(23).withMinute(59).withSecond(59);
            case WEEKLY -> now.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59);
            case MONTHLY -> now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            case YEARLY -> now.withDayOfYear(now.toLocalDate().lengthOfYear()).withHour(23).withMinute(59).withSecond(59);
        };
    }

    private String generateChartAsBase64(List<IncomeByPeriodDTO> data) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (IncomeByPeriodDTO dto : data) {
            dataset.setValue(dto.getPeriod(), dto.getTotal());
        }

        JFreeChart chart = ChartFactory.createPieChart("Distribución de Ingresos por Período", dataset, true, true, false);
        BufferedImage chartImage = chart.createBufferedImage(600, 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    // ========= Ingresos por Servicio (Resumen) =========

    public List<IncomeByServiceDTO> getIncomeByService() {
        return financialReportRepository.findIncomeByService();
    }

    public byte[] generateIncomeByServicePdf(List<IncomeByServiceDTO> data) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Reporte de Ingresos por Servicio");

            double total = data.stream().mapToDouble(IncomeByServiceDTO::getTotal).sum();
            model.put("total", String.format("S/%,.2f", total));

            return pdfGenerator.generatePdf("reports/financial/income-by-service", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar el PDF de ingresos por servicio", e);
        }
    }

    // ========= Ingresos por Especie =========

    public List<IncomeBySpecieDTO> getIncomeBySpecie() {
        return financialReportRepository.findIncomeBySpecie();
    }

    public byte[] generateIncomeBySpeciePdf(List<IncomeBySpecieDTO> data) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Reporte de Ingresos por Especie");

            double total = data.stream().mapToDouble(IncomeBySpecieDTO::getTotal).sum();
            model.put("total", String.format("S/%,.2f", total));

            return pdfGenerator.generatePdf("reports/financial/income-by-specie", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar el PDF de ingresos por especie", e);
        }
    }

    // ========= Ingresos por Método de Pago =========

    public List<PaymentMethodUsageDTO> getIncomeByPaymentMethod() {
        return financialReportRepository.findIncomeByPaymentMethod();
    }

    public byte[] generatePaymentMethodExcel(List<PaymentMethodUsageDTO> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            String[] headers = { "Método de Pago", "Total (S/.)", "Cantidad" };
            Sheet sheet = workbook.createSheet("Métodos de Pago");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (PaymentMethodUsageDTO dto : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getMethodName());
                row.createCell(1).setCellValue(dto.getTotal());
                row.createCell(2).setCellValue(dto.getCount());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return ExcelUtils.workbookToBytes(workbook);
        } catch (Exception e) {
            throw new ReportGenerationException("Error al generar el Excel de métodos de pago", e);
        }
    }

    // ========= Ingresos por Servicio + Período =========

    public List<IncomeByPeriodAndServiceDTO> getIncomeByPeriodAndService(ReportPeriod period) {
        LocalDateTime startDate = getStartDateByPeriod(period);
        LocalDateTime endDate = getEndDateByPeriod(period);

        return switch (period) {
            case DAILY -> financialReportRepository.findIncomeDaily(startDate, endDate);
            case WEEKLY -> financialReportRepository.findIncomeWeekly(startDate, endDate);
            case MONTHLY -> financialReportRepository.findIncomeMonthly(startDate, endDate);
            case YEARLY -> financialReportRepository.findIncomeYearly(startDate, endDate);
        };
    }

    public byte[] generateIncomeByPeriodAndServicePdf(List<IncomeByPeriodAndServiceDTO> data, ReportPeriod period) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Reporte de Ingresos por Servicio");
            model.put("period", period.getDisplayName());

            Map<String, List<IncomeByPeriodAndServiceDTO>> grouped = data.stream()
                    .collect(Collectors.groupingBy(dto -> {
                        if (period == ReportPeriod.WEEKLY) {
                            return getFormattedWeekRange(dto.getPeriod());
                        }
                        return dto.getPeriod();
                    }));

            model.put("groupedData", grouped);

            return pdfGenerator.generatePdf("reports/financial/income-by-period-service", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar PDF de ingresos por servicio y periodo", e);
        }
    }

    private String getFormattedWeekRange(String period) {
        try {
            String[] parts = period.split("/");
            int week = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            LocalDate startOfWeek = LocalDate.of(year, 1, 1)
                    .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                    .with(DayOfWeek.MONDAY);

            LocalDate endOfWeek = startOfWeek.plusDays(6);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return formatter.format(startOfWeek) + " - " + formatter.format(endOfWeek);
        } catch (Exception e) {
            return period; // fallback en caso de error
        }
    }
}
