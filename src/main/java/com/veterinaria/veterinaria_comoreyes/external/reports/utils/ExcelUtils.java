package com.veterinaria.veterinaria_comoreyes.external.reports.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {
    
    public static byte[] workbookToBytes(Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error al generar Excel", e);
        }
    }
}