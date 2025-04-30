package com.veterinaria.veterinaria_comoreyes.util;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;

public abstract class FilterStatus {
    @PersistenceContext
    protected EntityManager entityManager;

    public void activeFilterStatus(boolean status) {
        entityManager.unwrap(Session.class).enableFilter("statusActive").setParameter("status", status);
    }
}
