package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;

public interface IUserService {
    
    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);


    void updatePassword(Long id, String newPassword);

    void validateEmailForNewUser(String email);

    User getUserByEmailForAuth(String email);

}
