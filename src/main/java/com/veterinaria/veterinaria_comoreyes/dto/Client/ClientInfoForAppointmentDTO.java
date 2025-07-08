package com.veterinaria.veterinaria_comoreyes.dto.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoForAppointmentDTO {
    private String clientId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
}
