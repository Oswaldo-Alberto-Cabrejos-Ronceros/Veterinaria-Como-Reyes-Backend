package com.veterinaria.veterinaria_comoreyes.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "headquarter_vet_service")
@Filter(name  ="estadoActivo", condition = "estado = :estado")
public class HeadquarterVetService extends EntityWithStatus{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToAny(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarter;

    @ManyToAny(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private VeterinaryService veterinaryService;

}
