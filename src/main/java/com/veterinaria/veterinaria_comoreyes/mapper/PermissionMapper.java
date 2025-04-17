package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;

public class PermissionMapper {

    public static PermissionDTO maptoPermissionDTO (Permission permission) {
        return new PermissionDTO(
                permission.getPermissionId(),
                permission.getActionCode(),
                permission.getModule(),
                permission.getDescription());
    }

    public static Permission maptoPermission (PermissionDTO permissionDTO) {
        return new Permission(
                permissionDTO.getPermissionId(),
                permissionDTO.getActionCode(),
                permissionDTO.getModule(),
                permissionDTO.getDescription());
    }
}
