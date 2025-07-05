package com.veterinaria.veterinaria_comoreyes.dto.Client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfoPanelAdminDTO {
    private Long clientId;
    private String fullName;
    private String initials;
    private String phone;
}
