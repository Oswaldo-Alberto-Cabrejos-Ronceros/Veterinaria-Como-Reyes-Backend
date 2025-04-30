package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;


import java.util.List;

public interface IRoleService {
   RoleDTO getRoleById(Long id);

    List<RoleDTO> getAllRoles();

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(Long id, RoleDTO roleDTO);

    void deleteRole(Long id);

    List<Role> validateAndFetchRoles(List<RoleDTO> roleDTOs);

}
