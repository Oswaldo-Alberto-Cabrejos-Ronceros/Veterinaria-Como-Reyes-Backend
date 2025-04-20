package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.UserRepository;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.maptoUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return UserMapper.maptoUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::maptoUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalStateException("Ya existe un usuario con el correo ingresado");
        }
        User user = UserMapper.maptoUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.maptoUserDTO(savedUser);
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setType(userDTO.getType());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setStatus(userDTO.getStatus());

        User updatedUser = userRepository.save(user);
        return UserMapper.maptoUserDTO(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Cambio de estado a inactivo
        user.setStatus(0); // 0 = inactivo
        userRepository.save(user);
    }
}
