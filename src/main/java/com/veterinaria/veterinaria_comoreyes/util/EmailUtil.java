package com.veterinaria.veterinaria_comoreyes.util;

import com.veterinaria.veterinaria_comoreyes.exception.EmailAlreadyExistsException;
import com.veterinaria.veterinaria_comoreyes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private final UserRepository userRepository;

    // Constructor de inyección de dependencias
    @Autowired
    public EmailUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Validar si el email ya existe
    public void validateEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Ya existe un usuario registrado con el correo: " + email);
        }
    }
}
