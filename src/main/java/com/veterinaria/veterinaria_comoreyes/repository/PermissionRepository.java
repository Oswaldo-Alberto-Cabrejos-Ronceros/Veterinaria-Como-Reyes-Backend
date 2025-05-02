package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionIdAndStatusIsTrue(Long id);
}
