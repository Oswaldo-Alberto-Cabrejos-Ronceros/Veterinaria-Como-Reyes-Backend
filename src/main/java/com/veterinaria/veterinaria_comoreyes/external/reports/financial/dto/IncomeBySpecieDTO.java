package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

public class IncomeBySpecieDTO {
    private String specieName;
    private Double total;
    private Long count;

    public IncomeBySpecieDTO(String specieName, Number total, Number count) {
        this.specieName = specieName;
        this.total = total != null ? total.doubleValue() : 0.0;
        this.count = count != null ? count.longValue() : 0L;
    }

    public String getSpecieName() {
        return specieName;
    }

    public Double getTotal() {
        return total;
    }

    public Long getCount() {
        return count;
    }

    public String getFormattedTotal() {
        return String.format("S/%,.2f", total);
    }
}
