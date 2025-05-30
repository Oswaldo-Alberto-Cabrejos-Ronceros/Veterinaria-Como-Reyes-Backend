package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Permission.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Permission;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = GlobalMapperConfig.class,componentModel = "spring")
public interface PermissionMapper {

    PermissionDTO maptoPermissionDTO(Permission permission);

    Permission maptoPermission(PermissionDTO permissionDTO);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissionId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePermissionFromDTO(PermissionDTO dto, @MappingTarget Permission entity);

    default List<PermissionDTO> mapToPermissionDTOList(List<Permission> permissions) {
        if (permissions == null) return Collections.emptyList();
        return permissions.stream()
                .map(this::maptoPermissionDTO)
                .collect(Collectors.toList());
    }

    // Si quieres mapear roles a solo los IDs
    default Set<Long> mapRoleIds(List<Role> roles) {
        if (roles == null) return Collections.emptySet();
        return roles.stream().map(Role::getRoleId).collect(Collectors.toSet());
    }
}
