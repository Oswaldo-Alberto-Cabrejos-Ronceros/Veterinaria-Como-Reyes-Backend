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
    @GeneratedValue
    private Long appointmentId;

    private LocalDateTime scheduleDateTime;

    private LocalDateTime creationDate;

    private String comment;

    private String cancellationNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_appointments")
    private StatusAppointment statusAppointment;

    @ManyToOne
    @JoinColumn(name = "id_headquarter_vetService")
    private HeadquarterVetService headquarterVetService;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "id_animal")
    private Animal animal;
}
