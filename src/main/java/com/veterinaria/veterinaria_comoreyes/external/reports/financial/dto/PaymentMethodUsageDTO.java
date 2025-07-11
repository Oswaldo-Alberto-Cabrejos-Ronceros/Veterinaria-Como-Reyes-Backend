package com.veterinaria.veterinaria_comoreyes.external.reports.financial.dto;

public class PaymentMethodUsageDTO {
    private String methodName;
    private Double total;
    private Long count;

    public PaymentMethodUsageDTO(String methodName, Number total, Number count) {
        this.methodName = methodName;
        this.total = total != null ? total.doubleValue() : 0.0;
        this.count = count != null ? count.longValue() : 0L;
    }

    public String getMethodName() {
        return methodName;
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
