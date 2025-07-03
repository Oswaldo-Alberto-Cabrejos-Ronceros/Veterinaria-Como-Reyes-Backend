package com.veterinaria.veterinaria_comoreyes.external.reniec.service;

import com.veterinaria.veterinaria_comoreyes.external.reniec.dto.ReniecResponseDTO;
import com.veterinaria.veterinaria_comoreyes.external.reniec.dto.ReniecResponseSimpleDTO;

public interface IReniecService {

    ReniecResponseDTO consultDni(String dni);

    void validateIdentityReniec(String dni, String name, String lastName);

    ReniecResponseSimpleDTO consultDniSimple(String dni);
}
