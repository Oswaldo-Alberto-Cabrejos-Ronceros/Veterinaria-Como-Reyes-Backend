package com.veterinaria.veterinaria_comoreyes.util;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
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
        boolean exist= headquarterRepository.existsByHeadquarterIdAndStatusIsTrue(id);
        if(!exist){
            throw new HeadquarterNotValidException("Headquarter not available");
        }
    }
}
