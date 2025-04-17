package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private long permissionId;
    private String actionCode;
    private String module;
    private String description;
}
