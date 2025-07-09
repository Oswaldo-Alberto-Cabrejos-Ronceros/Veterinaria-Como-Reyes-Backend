package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

public class IncomeByPeriodDTO {
    private String period;
    private Double total;
    
    // Constructor modificado para aceptar diferentes tipos numéricos
    public IncomeByPeriodDTO(String period, Number total) {
        this.period = period;
        this.total = total != null ? total.doubleValue() : 0.0;
    }
    
    // Getters
    public String getPeriod() {
        return period;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public String getFormattedTotal() {
        return String.format("S/%,.2f", total);
    }
}