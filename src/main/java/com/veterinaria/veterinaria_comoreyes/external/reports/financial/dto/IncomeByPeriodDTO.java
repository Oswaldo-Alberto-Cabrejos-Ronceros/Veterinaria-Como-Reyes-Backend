package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

public class IncomeByPeriodDTO {
    private String period;
    private Double total;
    
    // Constructor explícitamente mapeado para Hibernate
    public IncomeByPeriodDTO(String period, Double total) {
        this.period = period;
        this.total = total;
    }
    
    // Getters necesarios
    public String getPeriod() {
        return period;
    }
    
    public Double getTotal() {
        return total;
    }
    
    // Métodos adicionales
    public String getFormattedTotal() {
        return String.format("S/%,.2f", total);
    }
}