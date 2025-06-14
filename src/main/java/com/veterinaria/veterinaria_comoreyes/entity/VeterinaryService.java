package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//para filtro
@Filter(name = "statusActive", condition = "status = :status")
public class    VeterinaryService extends EntityWithStatus{
    @Id
    @GeneratedValue
    private Long serviceId;

    private String name;

    private String description;

    private Double price;

    private Integer duration;

    private String dirImage;

    private Long simultaneousCapacity;

    @ManyToOne
    @JoinColumn(name = "id_specie")
    private Specie specie;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

}
