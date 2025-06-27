package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Service.VetServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVeterinaryServiceService {
    VeterinaryServiceDTO getServiceById(Long id);

    List<VeterinaryServiceDTO> getAllServices();

    List<VeterinaryServiceDTO> getAllServicesBySpecie(Long specieId);

    List<VeterinaryServiceDTO> getAllServicesByCategory(Long categoryId);

    VeterinaryServiceDTO createService(VeterinaryServiceDTO veterinaryServiceDTO);

    VeterinaryServiceDTO updateService(Long id,VeterinaryServiceDTO veterinaryServiceDTO);

    void deleteService(Long id);

    Page<VetServiceListDTO> searchVetServices(String name, Double price, String specieId, String categoryId,
            Boolean status, Pageable pageable);
}
