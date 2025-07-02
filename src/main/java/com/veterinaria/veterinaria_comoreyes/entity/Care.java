package com.veterinaria.veterinaria_comoreyes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Care{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long careId;

    private LocalDateTime careDateTime;

    @Enumerated(EnumType.STRING)
    private StatusCare statusCare;

    @ManyToOne
    @JoinColumn(name = "headquarter_vetservice_id")
    private HeadquarterVetService headquarterVetService;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
