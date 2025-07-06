package com.veterinaria.veterinaria_comoreyes.dto.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatsPanelDTO {
    private Long totalClients;
    private Long currentMonth;
    private Long previousMonth;
    private String difference; // CAMBIADO de Long a String
}
