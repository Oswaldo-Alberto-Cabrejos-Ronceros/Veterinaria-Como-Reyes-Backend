package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Permission.PermissionDTO;

import java.util.List;
import java.util.Map;

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

    List<String> permissionsByRoleId(Long roleId);

    //Obtener todos los (name) de los permisos en el formato deseado para el front-end
    Map<String, List<String>> getGroupedPermissionsByRoleId(Long roleId);
}
