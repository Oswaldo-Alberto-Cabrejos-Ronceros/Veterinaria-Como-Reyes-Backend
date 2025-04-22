package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryServiceDTO;

import java.util.List;

public interface IVeterinaryServiceService {
    VeterinaryServiceDTO getServiceById(Long id);

    List<VeterinaryServiceDTO> getAllServices();

    List<VeterinaryServiceDTO> getAllServicesBySpecie(Long specieId);

    List<VeterinaryServiceDTO> getAllServicesByCategory(Long categoryId);

    VeterinaryServiceDTO createService(VeterinaryServiceDTO veterinaryServiceDTO);

    VeterinaryServiceDTO updateService(Long id,VeterinaryServiceDTO veterinaryServiceDTO);

    void deleteService(Long id);
}
