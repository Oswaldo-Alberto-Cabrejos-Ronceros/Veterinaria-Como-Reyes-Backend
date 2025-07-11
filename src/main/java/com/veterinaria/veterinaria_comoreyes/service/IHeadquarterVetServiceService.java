package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;
import java.util.Optional;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeBasicInfoDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter_Service.HeadquarterVetServiceDTO;
import org.springframework.transaction.annotation.Transactional;

public interface IHeadquarterVetServiceService {

    List<HeadquarterVetServiceDTO> callAllHeadquarterVetServiceByHeadquarterId();

    List<HeadquarterVetServiceDTO> callHeadquarterVetServiceByHeadquarter(Long headquarterId);

    Optional<HeadquarterVetServiceDTO> getHeadquarterVetServiceById(Long id);

    HeadquarterVetServiceDTO createHeadquarterVetService(HeadquarterVetServiceDTO headquarterVetServiceDTO);

    HeadquarterVetServiceDTO updateHeadquarterVetService(Long id, HeadquarterVetServiceDTO headquarterVetServiceDTO);

    void deleteHeadquarterVetService(Long id);

    void validateHeadquarterVetService(Long id);

    String nameService(Long id);

    String nameSpecie(Long id);

    Double priceService(Long id);

    List<EmployeeBasicInfoDTO> getVeterinariansByHvs(Long hvsId);

    @Transactional
    void updateSimultaneousCapacity(Long id, Integer newCapacity);

    @Transactional
    void enableHeadquarterVetService(Long id);


    //Get all status 
    List<HeadquarterVetServiceDTO> getAllHeadquarterVetServiceByHeadquarter(Long headquarterId);

}
