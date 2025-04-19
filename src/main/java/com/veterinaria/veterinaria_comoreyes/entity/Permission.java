package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permission")
    private long permissionId;

    @Column(name = "action_code", nullable = false, length = 50, unique = true)
    private String actionCode;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @Column(name = "description", length = 100)
    private String description;
}
