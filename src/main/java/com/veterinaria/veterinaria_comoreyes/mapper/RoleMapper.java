package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
public class RoleMapper {

    // Convertir de Role a RoleDTO
    public static RoleDTO mapToRoleDTO(Role role) {
        return new RoleDTO(
                role.getRoleId(),
                role.getName(),
                role.getDescription(),
                role.getStatus()
        );
    }

    // Convertir de RoleDTO a Role
    public static Role mapToRole(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getRoleId(),
                roleDTO.getName(),
                roleDTO.getDescription(),
                roleDTO.getStatus()
        );
    }
}
