package com.veterinaria.veterinaria_comoreyes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//para filtro
@FilterDef(name = "statusActive", parameters = @ParamDef(name = "status", type = Boolean.class))
@Filter(name = "statusActive", condition = "status = :status")
@MappedSuperclass
public abstract class EntityWithStatus {

    @Column(nullable = false)
    private Boolean status=true;
}
