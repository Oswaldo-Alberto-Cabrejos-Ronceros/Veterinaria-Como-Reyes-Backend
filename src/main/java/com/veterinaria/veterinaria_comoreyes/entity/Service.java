package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue
    private Long serviceId;

    private String description;

    private float price;

    private LocalTime duration;

    private String dirImage;

    @OneToOne
    private Specie specie;

    @OneToOne
    private Category category;

    private Byte status;

}
