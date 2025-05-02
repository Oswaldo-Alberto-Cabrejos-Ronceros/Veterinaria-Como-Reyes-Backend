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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//para filtro
@Filter(name = "statusActive", condition = "status = :status")
public class Role extends EntityWithStatus{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(length = 20)
    private String name;

    @Column(length = 60)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<Employee> employees;

    private Integer position;

}
