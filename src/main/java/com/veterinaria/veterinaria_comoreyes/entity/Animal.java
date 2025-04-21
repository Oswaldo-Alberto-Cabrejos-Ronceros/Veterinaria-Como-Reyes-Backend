package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalId;

    private String name;

    private String gender;

    private float weight;

    private LocalDate birthDate;

    private String comment;

    private String urlImage;

    @ManyToOne
    private Breed breed;

    @ManyToOne
    private Client client;

    private Byte status;
}
