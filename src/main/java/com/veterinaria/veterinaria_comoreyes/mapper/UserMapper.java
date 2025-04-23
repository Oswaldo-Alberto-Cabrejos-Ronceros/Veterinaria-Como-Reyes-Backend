package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.User;

public class UserMapper {

    public static UserDTO maptoUserDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getType(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus()
        );
    }

    public static User maptoUser(UserDTO userDTO) {
        return new User(
                userDTO.getUserId(),
                userDTO.getType(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getStatus()
        );
    }
}
