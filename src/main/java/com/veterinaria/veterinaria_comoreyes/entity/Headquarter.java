package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Headquarter {
    private long headquarterId;
    private String phone;
    private String address;
    private String email;
    private String district;
    private String province;
    private String department;
    private Integer status;
}
