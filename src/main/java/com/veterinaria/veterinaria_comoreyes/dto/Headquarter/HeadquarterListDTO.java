package com.veterinaria.veterinaria_comoreyes.dto.Headquarter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HeadquarterListDTO {
    private long headquarterId;
    private String name;
    private String address;
    private String district;
    private String status;
}
