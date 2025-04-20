package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;

public class RoleMapper {

    public static RoleDTO mapToRoleDTO(Role role) {
        return new RoleDTO(
                role.getRoleId(),
                role.getName(),
                role.getDescription(),
                role.getStatus()
        );
    }

    public static Role mapToRole(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getRoleId(),
                roleDTO.getName(),
                roleDTO.getDescription(),
                roleDTO.getStatus()
        );
    }

}
