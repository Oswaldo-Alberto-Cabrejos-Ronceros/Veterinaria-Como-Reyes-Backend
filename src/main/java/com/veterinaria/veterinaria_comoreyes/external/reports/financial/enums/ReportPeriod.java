package com.veterinaria.veterinaria_comoreyes.external.reports.financial.enums;

public enum ReportPeriod {
    DAILY("Día"),
    WEEKLY("Semana"),
    MONTHLY("Mes"),
    YEARLY("Año");
    
    private final String displayName;
    
    ReportPeriod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}