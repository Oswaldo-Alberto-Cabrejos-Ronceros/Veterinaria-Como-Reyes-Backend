package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.mapper.PermissionMapper;
import com.veterinaria.veterinaria_comoreyes.repository.PermissionRepository;
import com.veterinaria.veterinaria_comoreyes.repository.RoleRepository;
import com.veterinaria.veterinaria_comoreyes.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionRepository.existsByActionCode(permissionDTO.getActionCode())) {
            throw new RuntimeException("Ya existe un permiso con ese código de acción");
        }

        if (permissionRepository.existsByName(permissionDTO.getName())) {
            throw new RuntimeException("Ya existe un permiso con ese nombre");
        }

        Permission permission = permissionMapper.maptoPermission(permissionDTO);
        permission.setStatus(true);

        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.maptoPermissionDTO(savedPermission);
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));
        return permissionMapper.maptoPermissionDTO(permission);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.mapToPermissionDTOList(permissions);
    }

    @Override
    public List<PermissionDTO> getAllActivePermissions() {
        List<Permission> permissions = permissionRepository.findByStatusTrue();
        return permissionMapper.mapToPermissionDTOList(permissions);
    }

    @Override
    @Transactional
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));

        if (permissionDTO.getActionCode() != null &&
                !existingPermission.getActionCode().equals(permissionDTO.getActionCode())) {
            if (permissionRepository.existsByActionCode(permissionDTO.getActionCode())) {
                throw new RuntimeException("Ya existe otro permiso con ese código de acción");
            }
        }

        if (permissionDTO.getName() != null &&
                !existingPermission.getName().equals(permissionDTO.getName())) {
            if (permissionRepository.existsByName(permissionDTO.getName())) {
                throw new RuntimeException("Ya existe otro permiso con ese nombre");
            }
        }

        permissionMapper.updatePermissionFromDTO(permissionDTO, existingPermission);
        Permission updatedPermission = permissionRepository.save(existingPermission);
        return permissionMapper.maptoPermissionDTO(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));

        if (!permission.getRoles().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el permiso porque está asignado a roles");
        }

        permissionRepository.delete(permission);
    }

    @Override
    @Transactional
    public void togglePermissionStatus(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID: " + id));
        permission.setStatus(!permission.getStatus());
        permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public PermissionDTO assignRolesToPermission(Long permissionId, List<Long> roleIds) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        List<Role> roles = roleIds.isEmpty()
                ? Collections.emptyList()
                : roleRepository.findAllById(roleIds);

        permission.getRoles().addAll(roles);
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.maptoPermissionDTO(updatedPermission);
    }

    @Override
    @Transactional
    public PermissionDTO removeRolesFromPermission(Long permissionId, List<Long> roleIds) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));

        permission.getRoles().removeIf(role -> roleIds.contains(role.getRoleId()));
        Permission updatedPermission = permissionRepository.save(permission);
        return permissionMapper.maptoPermissionDTO(updatedPermission);
    }
}
