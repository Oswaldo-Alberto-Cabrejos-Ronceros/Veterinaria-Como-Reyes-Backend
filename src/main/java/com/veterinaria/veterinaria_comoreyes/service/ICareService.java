package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.Care.CareDTO;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;

public interface ICareService {

    CareDTO getCareById(Long id);

    List<CareDTO> getCareByAppointment(Long appointmentId);

    List<CareDTO> getAllCares();

    CareDTO createCare(CareDTO careDTO);

    CareDTO completeCare(Long id);

    CareDTO updateCare(Long id, CareDTO careDTO);

    UserBuyerDTO getInfoForPaymentMerPago(Long careId);

    // void deleteCare(Long id);
} 
