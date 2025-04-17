package com.veterinaria.veterinaria_comoreyes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterDTO {
    private long headquarterId;
    private String phone;
    private String address;
    private String email;
    private String district;
    private String province;
    private String department;
    private Integer status;
}
