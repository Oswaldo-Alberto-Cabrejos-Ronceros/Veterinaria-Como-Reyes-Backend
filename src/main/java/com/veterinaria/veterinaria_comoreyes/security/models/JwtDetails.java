package com.veterinaria.veterinaria_comoreyes.security.models;

public class JwtDetails {
    private final Long entityId;
    private final String type;

    public JwtDetails(Long entityId, String type) {
        this.entityId = entityId;
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getType() {
        return type;
    }
}