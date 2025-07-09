package com.veterinaria.veterinaria_comoreyes.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberToLetterConverter {
    private static final String[] UNIDADES = {
            "", "un", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"
    };

    private static final String[] DECENAS = {
            "diez", "once", "doce", "trece", "catorce", "quince",
            "dieciseis", "diecisiete", "dieciocho", "diecinueve",
            "veinte", "treinta", "cuarenta", "cincuenta",
            "sesenta", "setenta", "ochenta", "noventa"
    };

    private static final String[] CENTENAS = {
            "", "ciento", "doscientos", "trescientos", "cuatrocientos",
            "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"
    };

    public static String convert(double number) {
        BigDecimal bd = BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP);
        long enteros = bd.longValue();
        int decimales = bd.remainder(BigDecimal.ONE).multiply(new BigDecimal(100)).intValue();

        String convertido = convertNumber(enteros) + " soles";

        if (decimales > 0) {
            convertido += " con " + convertNumber(decimales) + " céntimos";
        }

        return convertido;
    }

    private static String convertNumber(long number) {
        if (number == 0)
            return "cero";
        if (number < 10)
            return UNIDADES[(int) number];
        if (number < 30)
            return DECENAS[(int) number - 10];
        if (number < 100) {
            long unidad = number % 10;
            return DECENAS[(int) (number / 10) + 8] +
                    (unidad > 0 ? " y " + UNIDADES[(int) unidad] : "");
        }
        if (number < 1000) {
            long centena = number / 100;
            long resto = number % 100;
            return CENTENAS[(int) centena] +
                    (resto > 0 ? " " + convertNumber(resto) : "");
        }
        if (number < 1000000) {
            long miles = number / 1000;
            long resto = number % 1000;
            return (miles == 1 ? "mil" : convertNumber(miles) + " mil") +
                    (resto > 0 ? " " + convertNumber(resto) : "");
        }
        return "Número demasiado grande";
    }
}