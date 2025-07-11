package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

public class IncomeByPeriodAndServiceDTO {
    private String period;
    private String serviceName;
    private Double unitPrice;
    private Long count;
    private Double total;
    private String weekRange;

    public IncomeByPeriodAndServiceDTO(String period, String serviceName, Number unitPrice, Number count) {
        this.period = period;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice != null ? unitPrice.doubleValue() : 0.0;
        this.count = count != null ? count.longValue() : 0L;
        this.total = this.unitPrice * this.count;
    }

    public String getPeriod() {
        return period;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Long getCount() {
        return count;
    }

    public Double getTotal() {
        return total;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
    }

    public String getFormattedTotal() {
        return String.format("S/%,.2f", total);
    }

    public void setWeekRange(String startDateFormatted, String endDateFormatted) {
        this.weekRange = startDateFormatted + " - " + endDateFormatted;
    }
}
