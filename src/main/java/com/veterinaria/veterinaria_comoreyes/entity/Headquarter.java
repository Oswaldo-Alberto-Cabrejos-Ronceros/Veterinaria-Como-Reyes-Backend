package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "headquarters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Headquarter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_headquarter")
    private long headquarterId;

    @Column(name = "phone", nullable = false, length = 9)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @Column(name = "province", nullable = false, length = 50)
    private String province;

    @Column(name = "departament", nullable = false, length = 50)
    private String department;

    @Column(name = "status", nullable = false)
    private Integer status;
}
