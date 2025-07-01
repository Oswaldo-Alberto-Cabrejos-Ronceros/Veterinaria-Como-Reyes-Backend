package com.veterinaria.veterinaria_comoreyes.dto.Client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientListDTO {
    private Long clientId;
    private String dni;
    private String name;
    private String lastName;
    private String headquarterName;
    private String status;
}
