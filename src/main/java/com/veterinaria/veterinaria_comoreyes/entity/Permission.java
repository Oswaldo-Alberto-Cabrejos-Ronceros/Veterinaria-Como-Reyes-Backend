package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "actionCode"),
                @UniqueConstraint(columnNames = "name")
        }
)
@Filter(name = "statusActive", condition = "status = :status")
public class Permission extends EntityWithStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @Column(unique = true)
    private String actionCode; // Ej: "animal_update"

    @Column(unique = true, length = 50)
    private String name;       // Ej: "Actualizar animal"

    @Column(length = 200)
    private String description; // Ej: "Permite modificar la información básica de los animales en el sistema"

    private String module;     // Ej: "animales"

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}