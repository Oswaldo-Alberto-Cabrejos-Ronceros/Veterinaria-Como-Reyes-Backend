package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clientId;
    private String name;
    private String lastName;
    private String address;
    private String phone;
    private String dirImage;
    @OneToOne
    private Headquarter headquarter;
    @OneToOne
    private User user;
    private Byte status;
}
