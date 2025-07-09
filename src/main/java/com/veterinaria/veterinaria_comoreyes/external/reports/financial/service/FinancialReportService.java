package com.veterinaria.veterinaria_comoreyes.external.reports.financial.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.veterinaria.veterinaria_comoreyes.entity.PaymentStatus;
import com.veterinaria.veterinaria_comoreyes.exception.ReportGenerationException;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto.IncomeByPeriodDTO;
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

    public List<IncomeByPeriodDTO> getIncomeByPeriod(ReportPeriod period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = getStartDateByPeriod(period);
        String pattern = getDatePattern(period);

        return financialReportRepository.findIncomeByPeriod(
                startDate,
                endDate,
                pattern);
    }

    private LocalDateTime getStartDateByPeriod(ReportPeriod period) {
        return switch (period) {
            case DAILY -> LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            case WEEKLY -> LocalDateTime.now().minusDays(7);
            case MONTHLY -> LocalDateTime.now().minusMonths(1);
            case YEARLY -> LocalDateTime.now().minusYears(1);
        };
    }

    private String getDatePattern(ReportPeriod period) {
        return switch (period) {
            case DAILY -> "YYYY-MM-DD";
            case WEEKLY -> "YYYY-IW";
            case MONTHLY -> "YYYY-MM";
            case YEARLY -> "YYYY";
        };
    }

    public byte[] generateIncomeByPeriodPdf(List<IncomeByPeriodDTO> data, ReportPeriod period) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "Reporte de Ingresos");
            model.put("period", period.getDisplayName());

            // Calcular total general - Cambiado de total() a getTotal()
            double total = data.stream().mapToDouble(IncomeByPeriodDTO::getTotal).sum();
            model.put("total", String.format("S/%,.2f", total));

            return pdfGenerator.generatePdf("reports/financial/income-by-period", model);
        } catch (IOException e) {
            throw new ReportGenerationException("Error al generar el reporte PDF", e);
        }
    }

    public byte[] generateIncomeByPeriodExcel(List<IncomeByPeriodDTO> data, ReportPeriod period) {
        try (Workbook workbook = new XSSFWorkbook()) {
            String[] headers = { "Periodo", "Total (S/.)" };
            String sheetName = "Ingresos " + period.getDisplayName();

            Sheet sheet = workbook.createSheet(sheetName);

            // Estilo para moneda
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("S/#,##0.00"));

            // Cabeceras
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Datos
            // Datos
            int rowNum = 1;
            for (IncomeByPeriodDTO item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getPeriod()); // Cambiado de period() a getPeriod()

                Cell amountCell = row.createCell(1);
                amountCell.setCellValue(item.getTotal()); // Cambiado de total() a getTotal()
                amountCell.setCellStyle(currencyStyle);
            }
            // Total
            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(0).setCellValue("TOTAL");

            Cell totalCell = totalRow.createCell(1);
            totalCell.setCellFormula(String.format("SUM(B2:B%d)", rowNum));
            totalCell.setCellStyle(currencyStyle);

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            return ExcelUtils.workbookToBytes(workbook);
        } catch (Exception e) {
            throw new ReportGenerationException("Error al generar el reporte Excel: " + e.getMessage(), e);
        }
    }
}