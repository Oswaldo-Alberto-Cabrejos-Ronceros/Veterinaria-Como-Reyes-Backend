package com.veterinaria.veterinaria_comoreyes.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Appointment{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_seq")
    @SequenceGenerator(name = "appointment_seq", sequenceName = "APPOINTMENT_SEQ", allocationSize = 1)
    @Column(name = "appointment_id")
    private Long appointmentId;


    private LocalDateTime scheduleDateTime;

    private LocalDateTime creationDate;

    @Column(name = "comentario")
    private String comment;

    private String cancellationNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_appointments")
    private StatusAppointment statusAppointment;

    @ManyToOne
    @JoinColumn(name = "headquarter_vetservice_id")
    private HeadquarterVetService headquarterVetService;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = true)
    private Animal animal;
}
