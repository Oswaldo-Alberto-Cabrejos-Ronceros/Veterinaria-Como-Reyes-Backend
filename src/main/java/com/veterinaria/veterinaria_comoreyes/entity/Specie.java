package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "species")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specie")
    private long specieId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "image_path", nullable = false, length = 255)
    private String imagePath;

    @Column(name = "status", nullable = false)
    private Integer status;
}
