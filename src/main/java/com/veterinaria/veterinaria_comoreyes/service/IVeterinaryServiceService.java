package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.Service.ServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.ServicesInfoTopPanelAdminDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IVeterinaryServiceService {
    VeterinaryServiceDTO getServiceById(Long id);

    List<VeterinaryServiceDTO> getAllServices();

    List<VeterinaryServiceDTO> getAllServicesBySpecie(Long specieId);

    List<VeterinaryServiceDTO> getAllServicesByCategory(Long categoryId);

    VeterinaryServiceDTO createService(VeterinaryServiceDTO veterinaryServiceDTO);

    VeterinaryServiceDTO updateService(Long id,VeterinaryServiceDTO veterinaryServiceDTO);

    void deleteService(Long id);

    @Transactional
    void activateVeterinaryService(Long serviceId);

    Page<ServiceListDTO> searchServices(String name, String specie, String category, Boolean status, Pageable pageable);

    List<ServicesInfoTopPanelAdminDTO> getTopServicesPanelAdmin();

    List<ServicesInfoTopPanelAdminDTO> getTopServicesPanelManager(Long headquarterId);
}
