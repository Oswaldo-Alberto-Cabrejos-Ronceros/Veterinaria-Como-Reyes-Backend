package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//para filtro
@Filter(name = "statusActive", condition = "status = :status")
public class Animal extends EntityWithStatus{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long animalId;

    private String name;

    private String gender;

    private float weight;

    private LocalDate birthDate;

    private String animalComment;

    private String urlImage;

    @ManyToOne
    private Breed breed;

    @ManyToOne
    private Client client;

}
