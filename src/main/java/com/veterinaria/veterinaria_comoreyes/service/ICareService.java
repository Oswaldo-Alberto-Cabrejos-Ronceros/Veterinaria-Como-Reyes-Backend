package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.RecentPatientsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.*;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICareService {

    CareDTO getCareById(Long id);

    List<CareDTO> getCareByAppointment(Long appointmentId);

    List<CareDTO> getAllCares();

    CareDTO createCare(CareDTO careDTO);

    CareDTO completeCare(Long id);

    CareDTO onGoingCare(Long id);

    CareDTO updateCare(Long id, CareDTO careDTO);

    UserBuyerDTO getInfoForPaymentMerPago(Long careId);

    @Transactional
    CareDTO createCareFromAppointment(CreateCareFromAppointmentDTO dto);

    @Transactional
    CareDTO createCareFromRequest(CareRequestDTO dto);

    Page<CareListDTO> searchCares(String fecha, Long idHeadquarter, Long idService, String estado, Pageable pageable);

    List<CareAndAppointmentPanelEmployeeDTO> getCaresForEmployee(Long employeeId);

    CareStatsTodayDTO getCareStatsToday();

    List<CareAndAppointmentPanelEmployeeDTO> getCaresByHeadquarterId(Long headquarterId);

    List<RecentPatientsDTO> getRecentPatients(Long employeeId);

    // void deleteCare(Long id);
} 
