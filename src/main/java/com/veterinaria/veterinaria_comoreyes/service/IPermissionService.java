package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;

import java.util.List;

public interface IPermissionService {
    PermissionDTO getPermissionById(Long id);

    List<PermissionDTO> getAllPermissions();

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);
}
