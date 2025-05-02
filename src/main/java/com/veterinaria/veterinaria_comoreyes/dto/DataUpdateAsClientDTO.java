package com.veterinaria.veterinaria_comoreyes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataUpdateAsClientDTO {
    private String address;
    private String phone;
    private Long headquarterId;
}
