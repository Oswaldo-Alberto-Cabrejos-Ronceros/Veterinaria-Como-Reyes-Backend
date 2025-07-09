package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

public class IncomeByServiceDTO {
    private String serviceName;
    private String category;
    private Double total;
    private Long count;

    public IncomeByServiceDTO(String serviceName, String category, Number total, Number count) {
        this.serviceName = serviceName;
        this.category = category;
        this.total = total != null ? total.doubleValue() : 0.0;
        this.count = count != null ? count.longValue() : 0L;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCategory() {
        return category;
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
