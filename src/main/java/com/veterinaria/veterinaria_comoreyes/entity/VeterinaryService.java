package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//para filtro
@Filter(name = "statusActive", condition = "status = :status")
public class VeterinaryService extends EntityWithStatus{
    @Id
    @GeneratedValue
    private Long serviceId;

    private String name;

    private String description;

    private float price;

    private LocalTime duration;

    private String dirImage;

    @OneToOne
    private Specie specie;

    @OneToOne
    private Category category;

}
