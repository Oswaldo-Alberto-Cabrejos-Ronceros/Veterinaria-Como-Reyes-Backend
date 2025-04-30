package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Headquarter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long headquarterId;

    @Column(unique = true,length = 40)
    private String name;

    @Column(unique = true, length = 9)
    private String phone;

    @Column(length = 150)
    private String address;

    @Column(unique = true,length = 255)
    private String email;

    @Column(length = 50)
    private String district;

    @Column(length = 50)
    private String province;

    @Column(length = 50)
    private String department;

    @Column(nullable = false, columnDefinition = "NUMBER(1,0)")
    private Byte status;

}
