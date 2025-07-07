package com.veterinaria.veterinaria_comoreyes.dto.Headquarter;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterListDTO {

    private long headquarterId;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String district;
    private String province;
}
