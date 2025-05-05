package com.veterinaria.veterinaria_comoreyes.mapper;
import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;
import com.veterinaria.veterinaria_comoreyes.entity.Role;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class PermissionMapper {

    public static PermissionDTO mapToPermissionDTO(Permission permission) {
        if (permission == null) {
            return null;
        }

        return PermissionDTO.builder()
                .permissionId(permission.getPermissionId())
                .actionCode(permission.getActionCode())
                .name(permission.getName())
                .description(permission.getDescription())
                .module(permission.getModule())
                .status(permission.getStatus())
                .roleIds(mapRoleIds(permission.getRoles())) // Cambio clave aquí
                .build();
    }

    public static Permission mapToPermission(PermissionDTO permissionDTO) {
        if (permissionDTO == null) {
            return null;
        }

        Permission permission = new Permission();
        permission.setPermissionId(permissionDTO.getPermissionId());
        permission.setActionCode(permissionDTO.getActionCode());
        permission.setName(permissionDTO.getName());
        permission.setDescription(permissionDTO.getDescription());
        permission.setModule(permissionDTO.getModule());
        permission.setStatus(permissionDTO.getStatus());

        return permission;
    }

    public static List<PermissionDTO> mapToPermissionDTOList(List<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptyList();
        }
        return permissions.stream()
                .map(PermissionMapper::mapToPermissionDTO)
                .collect(Collectors.toList());
    }

    // Cambiado para aceptar List<Role> en lugar de Set<Role>
    private static Set<Long> mapRoleIds(List<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(Role::getRoleId)
                .collect(Collectors.toSet());
    }

    public static void updatePermissionFromDTO(PermissionDTO source, Permission target) {
        if (source == null || target == null) return;

        if (source.getActionCode() != null) target.setActionCode(source.getActionCode());
        if (source.getName() != null) target.setName(source.getName());
        if (source.getDescription() != null) target.setDescription(source.getDescription());
        if (source.getModule() != null) target.setModule(source.getModule());
        if (source.getStatus() != null) target.setStatus(source.getStatus());
    }
}