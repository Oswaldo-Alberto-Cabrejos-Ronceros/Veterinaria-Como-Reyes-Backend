package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;
import com.veterinaria.veterinaria_comoreyes.mapper.PermissionMapper;
import com.veterinaria.veterinaria_comoreyes.repository.PermissionRepository;
import com.veterinaria.veterinaria_comoreyes.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return PermissionMapper.maptoPermissionDTO(permission);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(PermissionMapper::maptoPermissionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        Permission permission = PermissionMapper.maptoPermission(permissionDTO);
        Permission saved = permissionRepository.save(permission);
        return PermissionMapper.maptoPermissionDTO(saved);
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        permission.setActionCode(permissionDTO.getActionCode());
        permission.setModule(permissionDTO.getModule());
        permission.setDescription(permissionDTO.getDescription());

        Permission updated = permissionRepository.save(permission);
        return PermissionMapper.maptoPermissionDTO(updated);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        permissionRepository.delete(permission);
    }
}
