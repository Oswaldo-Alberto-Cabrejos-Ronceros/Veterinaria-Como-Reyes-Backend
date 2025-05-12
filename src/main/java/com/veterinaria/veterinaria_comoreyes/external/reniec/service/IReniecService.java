package com.veterinaria.veterinaria_comoreyes.external.reniec.service;

import com.veterinaria.veterinaria_comoreyes.external.reniec.dto.ReniecResponseDTO;

public interface IReniecService {

    ReniecResponseDTO consultDni(String dni);

    void validateIdentityReniec(String dni, String name, String lastName);
}
