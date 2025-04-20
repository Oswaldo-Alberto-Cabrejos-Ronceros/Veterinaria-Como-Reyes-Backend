package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String dni;

    private String cmvp;

    private String name;

    private String lastName;

    private String address;

    private String phone;

    private String dirImage;

    @OneToOne
    private Headquarter headquarter;

    @OneToOne
    private User user;

    @OneToOne
    private Role role;

    private Byte status;

}
