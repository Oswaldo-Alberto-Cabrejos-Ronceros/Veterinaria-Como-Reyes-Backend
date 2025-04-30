package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;
import com.veterinaria.veterinaria_comoreyes.mapper.PermissionMapper;
import com.veterinaria.veterinaria_comoreyes.repository.PermissionRepository;
import com.veterinaria.veterinaria_comoreyes.service.IPermissionService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements IPermissionService {


    private PermissionRepository permissionRepository;
    private FilterStatus filterStatus;
    public PermissionServiceImpl(PermissionRepository permissionRepository, FilterStatus filterStatus){
        this.permissionRepository=permissionRepository;
        this.filterStatus=filterStatus;
    }


    @Override
    public PermissionDTO getPermissionById(Long id) {
        filterStatus.activeFilterStatus(true);
        Permission permission = permissionRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return PermissionMapper.maptoPermissionDTO(permission);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        filterStatus.activeFilterStatus(true);
        return permissionRepository.findAll().stream()
                .map(PermissionMapper::maptoPermissionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        filterStatus.activeFilterStatus(true);
        Permission permission = PermissionMapper.maptoPermission(permissionDTO);
        Permission saved = permissionRepository.save(permission);
        return PermissionMapper.maptoPermissionDTO(saved);
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        filterStatus.activeFilterStatus(true);
        Permission permission = permissionRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        permission.setActionCode(permissionDTO.getActionCode());
        permission.setModule(permissionDTO.getModule());
        permission.setDescription(permissionDTO.getDescription());

        Permission updated = permissionRepository.save(permission);
        return PermissionMapper.maptoPermissionDTO(updated);
    }

    @Override
    public void deletePermission(Long id) {
        filterStatus.activeFilterStatus(true);
        Permission permission = permissionRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        permissionRepository.delete(permission);
    }
}
