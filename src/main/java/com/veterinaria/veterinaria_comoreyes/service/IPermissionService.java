package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;

import java.util.List;

public interface IPermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);
    PermissionDTO getPermissionById(Long id);
    List<PermissionDTO> getAllPermissions();
    List<PermissionDTO> getAllActivePermissions();
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);
    void deletePermission(Long id);
    void togglePermissionStatus(Long id);
    PermissionDTO assignRolesToPermission(Long permissionId, List<Long> roleIds);
    PermissionDTO removeRolesFromPermission(Long permissionId, List<Long> roleIds);
}
