package com.veterinaria.veterinaria_comoreyes.external.mercadoPago.service;

import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;

public interface IMerPagoService {
    String createPreference(UserBuyerDTO userBuyerDTO);

    String createPreferenceByIdAppointment(Long idAppointment);

    String createPreferenceByIdCare(Long idCare);
}
