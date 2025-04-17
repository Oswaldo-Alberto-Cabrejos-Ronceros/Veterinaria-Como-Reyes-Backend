package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long userId;
    private String type;
    private String email;
    private String password;
    private Integer status;
}
