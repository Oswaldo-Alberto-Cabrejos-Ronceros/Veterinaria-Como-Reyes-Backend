package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Role.RoleBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Role.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;


import java.util.List;

public interface IRoleService {
   RoleDTO getRoleById(Long id);

    List<RoleDTO> getAllRoles();

 List<RoleDTO> getAllRolesAndStatusTrue();

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(Long id, RoleDTO roleDTO);

    void deleteRole(Long id);

    List<Role> validateAndFetchRoles(List<RoleDTO> roleDTOs);

    List<RoleBasicDTO> filterRolesStatusActive(List<Role> roles);

    long countActiveRolesForEmployee(Long employeeId);

    List<Role> getActiveRolesForEmployee(Long employeeId);
}
