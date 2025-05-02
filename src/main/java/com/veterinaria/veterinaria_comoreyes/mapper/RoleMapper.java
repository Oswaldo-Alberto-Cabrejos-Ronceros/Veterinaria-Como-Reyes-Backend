package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDTO mapToRoleDTO(Role role) {
        if (role == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setPosition(role.getPosition());

        return roleDTO;
    }

    public static Role mapToRole(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        Role role = new Role();
        role.setRoleId(roleDTO.getRoleId());
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setPosition(roleDTO.getPosition());

        // No configuramos la lista de empleados aquí para evitar referencias circulares
        return role;

    }

    public static List<RoleDTO> mapToRoleDTOList(List<Role> roleList) {
        if (roleList == null) {
            return null;
        }

        return roleList.stream()
                .map(RoleMapper::mapToRoleDTO)
                .collect(Collectors.toList());
    }

    public static List<Role> mapToRoleList(List<RoleDTO> roleDTOList) {
        if (roleDTOList == null) {
            return null;
        }

        return roleDTOList.stream()
                .map(RoleMapper::mapToRole)
                .collect(Collectors.toList());
    }
}