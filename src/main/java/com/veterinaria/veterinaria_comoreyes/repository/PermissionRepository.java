package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionIdAndStatusIsTrue(Long id);
    Optional<Permission> findByActionCode(String actionCode);
    Optional<Permission> findByName(String name);
    boolean existsByActionCode(String actionCode);
    boolean existsByName(String name);
    List<Permission> findByStatusTrue();

    List<Permission> findByRolesRoleIdAndStatusTrue(Long roleId);
}
