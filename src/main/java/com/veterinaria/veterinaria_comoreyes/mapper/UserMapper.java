package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper {

    UserDTO maptoUserDTO(User user);

    User maptoUser(UserDTO userDTO);

}
