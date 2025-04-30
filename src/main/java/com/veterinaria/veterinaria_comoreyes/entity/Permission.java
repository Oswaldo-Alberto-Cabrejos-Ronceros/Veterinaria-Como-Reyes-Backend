package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//para filtro
@Filter(name = "statusActive", condition = "status = :status")
public class Permission extends EntityWithStatus{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long permissionId;

    private String actionCode;

    private String module;

    private String description;
}
