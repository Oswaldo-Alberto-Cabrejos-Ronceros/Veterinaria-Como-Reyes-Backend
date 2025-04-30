package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.ReniecResponseDTO;

import java.util.Map;

public interface IReniecService {

    ReniecResponseDTO consultDni(String dni);
}
