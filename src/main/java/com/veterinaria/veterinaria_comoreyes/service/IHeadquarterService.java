package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterEmployeesDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterListDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHeadquarterService {

    HeadquarterDTO getHeadquarterById(Long id);

    List<HeadquarterDTO> getAllHeadquarters();

    HeadquarterDTO createHeadquarter(HeadquarterDTO headquarterDTO);

    HeadquarterDTO updateHeadquarter(Long id, HeadquarterDTO headquarterDTO);

    void deleteHeadquarter(Long id);

    // Validar si la sede existe y esta disponible
    void validateHeadquarterAvailable(Long id);

    List<HeadquarterEmployeesDTO> getAllActiveHeadquartersWithActiveEmployees();

    Page<HeadquarterListDTO> searcHeadquarters(String name, String address, String district, Boolean status,
            Pageable pageable);
}
