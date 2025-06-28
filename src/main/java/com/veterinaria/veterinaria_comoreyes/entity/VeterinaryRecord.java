package com.veterinaria.veterinaria_comoreyes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class VeterinaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_care", nullable = false)
    private Care care;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateCreated;

    @Column
    private String diagnosis;

    @Column
    private String treatment;

    @Column
    private String observations;

    @Column
    private String resultUrl;

    @Enumerated(EnumType.STRING)
    private StatusVeterinaryRecord statusVeterinaryRecord= StatusVeterinaryRecord.EN_CURSO;;

}