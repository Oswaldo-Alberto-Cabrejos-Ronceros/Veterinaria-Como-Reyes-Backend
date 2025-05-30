package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;
import java.util.Optional;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service.HeadquarterVetServiceDTO;

public interface IHeadquarterVetServiceService {

    List<HeadquarterVetServiceDTO> callAllHeadquarterVetServiceByHeadquarterId();

    List<HeadquarterVetServiceDTO> callHeadquarterVetServiceByHeadquarter(Long headquarterId);

    Optional<HeadquarterVetServiceDTO> getHeadquarterVetServiceById(Long id);

    HeadquarterVetServiceDTO createHeadquarterVetService(HeadquarterVetServiceDTO headquarterVetServiceDTO);

    HeadquarterVetServiceDTO updateHeadquarterVetService(Long id, HeadquarterVetServiceDTO headquarterVetServiceDTO);

    void deleteHeadquarterVetService(Long id);

}
