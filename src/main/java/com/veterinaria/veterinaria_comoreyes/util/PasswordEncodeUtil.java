package com.veterinaria.veterinaria_comoreyes.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncodeUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Método para cifrar la contraseña
    public String encodePassword(String rawPassword) {

        return passwordEncoder.encode(rawPassword);
    }

    // Método para verificar si la contraseña coincide
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
