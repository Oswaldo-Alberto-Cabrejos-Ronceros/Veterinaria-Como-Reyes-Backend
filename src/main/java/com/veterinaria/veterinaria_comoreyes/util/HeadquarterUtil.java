package com.veterinaria.veterinaria_comoreyes.util;

import com.veterinaria.veterinaria_comoreyes.exception.EmailAlreadyExistsException;
import com.veterinaria.veterinaria_comoreyes.exception.HeadquarterNotValidException;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterRepository;
import com.veterinaria.veterinaria_comoreyes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HeadquarterUtil {

    private final HeadquarterRepository headquarterRepository;

    // Constructor de inyección de dependencias
    @Autowired
    public HeadquarterUtil(HeadquarterRepository headquarterRepository) {
        this.headquarterRepository = headquarterRepository;
    }

    // Validar si la sede existe y esta disponible
    public void validateHeadquarterAvailable(Long id) {

        // Verificar si la sede existe
        if (!headquarterRepository.existsByHeadquarterId(id)) {
            // Lanza una excepción si la sede no existe
            throw new HeadquarterNotValidException("La sede no existe");
        }

        // Verificar el estado de la sede
        Byte status = headquarterRepository.findStatusById(id);

        // Comparar si la sede no está disponible
        if (status != null && status == 0) {
            // Lanza una excepción si la sede no está disponible
            throw new HeadquarterNotValidException("La sede no está disponible");
        }
    }
}
