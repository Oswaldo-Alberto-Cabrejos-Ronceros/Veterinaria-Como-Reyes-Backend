package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;

public interface IUserService {
    
    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
