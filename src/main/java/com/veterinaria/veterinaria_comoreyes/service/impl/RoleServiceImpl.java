package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.mapper.RoleMapper;
import com.veterinaria.veterinaria_comoreyes.repository.RoleRepository;
import com.veterinaria.veterinaria_comoreyes.service.IRoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        Role role= roleRepository.findById(id).orElseThrow(()->new RuntimeException("Role not found with id " + id));
        return RoleMapper.mapToRoleDTO(role);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(RoleMapper::mapToRoleDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = RoleMapper.mapToRole(roleDTO);
        Role savedRole = roleRepository.save(role);
        return RoleMapper.mapToRoleDTO(savedRole);
    }

    @Transactional
    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id).orElseThrow(()->new RuntimeException("Role not found with id " + id));
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setStatus(roleDTO.getStatus());
        Role updateRole = roleRepository.save(role);
        return RoleMapper.mapToRoleDTO(updateRole);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(()->new RuntimeException("Role not found with id " + id));
        role.setStatus((byte) 0);
    }

    @Override
    public List<Role> validateAndFetchRoles(List<RoleDTO> roleDTOs) {
        List<Role> roles = new ArrayList<>();

        for (RoleDTO dto : roleDTOs) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRoleId()));

            if (role.getStatus() == 0) {
                throw new RuntimeException("El rol " + role.getName() + " no está activo");
            }

            roles.add(role);
        }

        return roles;
    }

}
