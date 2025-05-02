package com.veterinaria.veterinaria_comoreyes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoClientDTO {
    private Long clientId;
    private Long userId;
    private String dni;
    private String names;
    private String LastNames;
    private String phone;
    private String headquarterName;
    private String role= "cliente";
}

