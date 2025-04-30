package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(length = 20)
    private String name;

    @Column(length = 60)
    private String description;

    @Column(nullable = false, columnDefinition = "NUMBER(1,0)")
    private Byte status;

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

}
