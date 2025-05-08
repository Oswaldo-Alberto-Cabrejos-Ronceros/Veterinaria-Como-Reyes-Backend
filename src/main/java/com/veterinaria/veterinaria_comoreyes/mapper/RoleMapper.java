package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.RoleDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface RoleMapper {

    RoleDTO mapToRoleDTO(Role role);

    Role mapToRole(RoleDTO roleDTO);

    List<RoleDTO> mapToRoleDTOList(List<Role> roleList);

    List<Role> mapToRoleList(List<RoleDTO> roleDTOList);
}
