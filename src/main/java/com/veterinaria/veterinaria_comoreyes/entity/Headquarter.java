package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//para filtro
@Filter(name = "statusActive", condition = "status = :status")
public class Headquarter extends EntityWithStatus {
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

}
