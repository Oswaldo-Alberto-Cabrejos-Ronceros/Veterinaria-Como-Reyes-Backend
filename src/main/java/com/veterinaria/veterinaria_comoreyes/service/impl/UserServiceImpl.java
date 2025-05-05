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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailUtil emailUtil) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
    }

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
        // Validar correo
        emailUtil.validateEmailAvailable(userDTO.getEmail());

        // Mapear el DTO a la entidad User
        User user = UserMapper.maptoUser(userDTO);

        // Encriptar la contraseña
        user.setPassword(passwordUtil.encodePassword(user.getPassword()));
        user.setStatus(true);

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(user);

        // Mapear el usuario guardado (User) a UserDTO y devolverlo
        return UserMapper.maptoUserDTO(savedUser);
    }


    @Transactional
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Actualizar los campos con los valores del DTO
        user.setType(userDTO.getType());
        user.setEmail(userDTO.getEmail());

        // Cifrar la nueva contraseña (si la contraseña está presente en el DTO)
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordUtil.encodePassword(userDTO.getPassword()));
        }

        // Guardar el usuario actualizado en la base de datos
        User updatedUser = userRepository.save(user);
        return UserMapper.maptoUserDTO(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findByUserIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Cambio de estado a inactivo
        user.setStatus(false);// false = inactivo
        userRepository.save(user);
    }

    //update only password
    @Override
    public void updatePassword(Long id, String newPassword){
        User user = userRepository.findByUserIdAndStatusTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        //send the new encrypted password
        user.setPassword(passwordUtil.encodePassword(newPassword));

        userRepository.save(user);
    }
}
