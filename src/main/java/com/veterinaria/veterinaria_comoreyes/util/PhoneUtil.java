package com.veterinaria.veterinaria_comoreyes.util;

import com.veterinaria.veterinaria_comoreyes.exception.PhoneAlreadyExistsException;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PhoneUtil {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    // Inyección de dependencias en el constructor
    @Autowired
    public PhoneUtil(ClientRepository clientRepository, EmployeeRepository employeeRepository) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
    }

    // Verificar número disponible
    public void validatePhoneAvailable(String phoneNumber, String type) {
        boolean exists;
        switch (type.toLowerCase()) {
            case "cliente":
                exists = clientRepository.existsByPhone(phoneNumber);
                break;
            case "empleado":
                exists = employeeRepository.existsByPhone(phoneNumber);
                break;
            default:
                throw new IllegalArgumentException("Tipo desconocido para validación de teléfono: " + type);
        }

        if (exists) {
            throw new PhoneAlreadyExistsException("El número de teléfono ya está registrado en otro " + type );
        }
    }

    // Método para generar código de 4 dígitos
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Genera entre 1000 y 9999
        return String.valueOf(code);
    }
}
