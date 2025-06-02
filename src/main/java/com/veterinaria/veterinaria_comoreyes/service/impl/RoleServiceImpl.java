package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Role.RoleBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Role.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import com.veterinaria.veterinaria_comoreyes.mapper.RoleMapper;
import com.veterinaria.veterinaria_comoreyes.repository.RoleRepository;
import com.veterinaria.veterinaria_comoreyes.service.IRoleService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;
    private final FilterStatus filterStatus;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, FilterStatus filterStatus, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.filterStatus = filterStatus;
        this.roleMapper = roleMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public RoleDTO getRoleById(Long id) {
        filterStatus.activeFilterStatus(true);
        Role role = roleRepository.findByRoleIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
        return roleMapper.mapToRoleDTO(role);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::mapToRoleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> getAllRolesAndStatusTrue() {
        filterStatus.activeFilterStatus(true);
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::mapToRoleDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        filterStatus.activeFilterStatus(true);
        Role role = roleMapper.mapToRole(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.mapToRoleDTO(savedRole);
    }

    @Transactional
    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        filterStatus.activeFilterStatus(true);
        Role role = roleRepository.findByRoleIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        Role updatedRole = roleRepository.save(role);
        return roleMapper.mapToRoleDTO(updatedRole);
    }

    @Transactional
    @Override
    public void deleteRole(Long id) {
        filterStatus.activeFilterStatus(true);
        Role role = roleRepository.findByRoleIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
        role.setStatus(false);
        roleRepository.save(role);
    }

    @Override
    public List<Role> validateAndFetchRoles(List<RoleDTO> roleDTOs) {
        List<Role> roles = new ArrayList<>();

        for (RoleDTO dto : roleDTOs) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + dto.getRoleId()));
            roles.add(role);
        }

        return roles;
    }
    @Override
    public List<RoleBasicDTO> filterRolesStatusActive(List<Role> roles) {
        List<RoleBasicDTO> rolesActives = roles.stream()
                .filter(role -> Boolean.TRUE.equals(role.getStatus()))
                .map(role -> {
                    RoleBasicDTO dto = new RoleBasicDTO();
                    dto.setId(role.getRoleId());
                    dto.setName(role.getName());
                    return dto;
                })
                .collect(Collectors.toList());

        return rolesActives.isEmpty() ? null : rolesActives;
    }


    //Obtener la cantidad de roles activos que tiebne un empleadoId (using in Auth)
    @Override
    public long countActiveRolesForEmployee(Long employeeId) {
        return roleRepository.countByEmployeesEmployeeIdAndStatusTrue(employeeId);
    }

    //Obtener la info de los roles activos de un empleadoId
    @Override
    public List<Role> getActiveRolesForEmployee(Long employeeId) {
        return roleRepository.findByEmployeesEmployeeIdAndStatusTrue(employeeId);
    }
}
