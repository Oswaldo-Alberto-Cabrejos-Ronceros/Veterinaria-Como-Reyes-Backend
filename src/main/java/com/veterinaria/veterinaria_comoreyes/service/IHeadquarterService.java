package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterEmployeesDTO;

import java.util.List;

public interface IHeadquarterService {

    HeadquarterDTO getHeadquarterById(Long id);

    List<HeadquarterDTO> getAllHeadquarters();

    HeadquarterDTO createHeadquarter(HeadquarterDTO headquarterDTO);

    HeadquarterDTO updateHeadquarter(Long id, HeadquarterDTO headquarterDTO);

    void deleteHeadquarter(Long id);

    // Validar si la sede existe y esta disponible
    void validateHeadquarterAvailable(Long id);

    List<HeadquarterEmployeesDTO> getAllActiveHeadquartersWithActiveEmployees();
}
