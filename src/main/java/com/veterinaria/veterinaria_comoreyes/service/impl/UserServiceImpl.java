package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.UserRepository;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.EmailUtil;
import com.veterinaria.veterinaria_comoreyes.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final PasswordUtil passwordUtil;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailUtil emailUtil, PasswordUtil passwordUtil, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
        this.passwordUtil = passwordUtil;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.maptoUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return userMapper.maptoUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::maptoUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        emailUtil.validateEmailAvailable(userDTO.getEmail());

        User user = userMapper.maptoUser(userDTO);
        user.setPassword(passwordUtil.encodePassword(user.getPassword()));
        user.setStatus(true);

        User savedUser = userRepository.save(user);
        return userMapper.maptoUserDTO(savedUser);
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setType(userDTO.getType());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordUtil.encodePassword(userDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.maptoUserDTO(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findByUserIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(false);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updatePassword(Long id, String newPassword) {
        User user = userRepository.findByUserIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setPassword(passwordUtil.encodePassword(newPassword));
        userRepository.save(user);
    }
}
