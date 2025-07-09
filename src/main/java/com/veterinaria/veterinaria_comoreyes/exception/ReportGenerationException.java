package com.veterinaria.veterinaria_comoreyes.exception;

public class ReportGenerationException extends RuntimeException {
    
    public ReportGenerationException(String message) {
        super(message);
    }
    
    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}