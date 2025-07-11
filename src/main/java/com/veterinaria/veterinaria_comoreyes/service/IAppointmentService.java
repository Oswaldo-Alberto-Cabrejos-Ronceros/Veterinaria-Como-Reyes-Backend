package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;
import java.util.Optional;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareAndAppointmentPanelEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentInfoForAppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.*;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    UserBuyerDTO getInfoForPaymentMerPago(Long idAppoinment);

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);

    void deleteAppointment(Long id);

    AppointmentResponseDTO confirmAppointment(Long id);

    AppointmentResponseDTO completeAppointment(Long id);

    List <TimesForTurnDTO> getAvailableTimesForTurn(Long headquarterVetServiceId, String date);

    List<BasicServiceForAppointmentDTO> getServicesByHeadquarterAndSpeciesForAppointment(Long headquarterId, Long speciesId);

    List<InfoBasicAppointmentForPanelDTO> getAppointmentsForClientPanel(Long clientId);

    Page<AppointmentListDTO> searchAppointments(String day, String headquarter, String categoryService, String appointmentStatus,
            Pageable pageable);

    List<AppointmentInfoPanelAdminDTO> getAppointmentsByDateForPanelAdmin();

    List<AppointmentInfoPanelAdminDTO> getAppointmentsInfoByDateAndHeadquarter(Long headquarterId);

    List<CareAndAppointmentPanelEmployeeDTO> getAppointmentsForEmployee(Long employeeId);

    AppointmentResponseDTO confirmAppointmentByEmail(Long id);

    AppointmentStatsTodayDTO getTodayAppointmentStats();

    AppointmentStatsTodayDTO getTodayAppointmentStatsByHeadquarter(Long headquarterId);

    Optional<InfoAppointmentForPanelDTO> getAppointmentInfoForPanel(Long appointmentId);

    Optional<AnimalInfoForAppointmentDTO> getAnimalInfoByAppointmentId(Long appointmentId);

    Optional<ClientInfoForAppointmentDTO> getClientInfoForAppointment(Long appointmentId);

    Optional<PaymentInfoForAppointmentDTO> getPaymentInfoByAppointmentId(Long appointmentId);

    AppointmentStatsForReceptionistDTO getStatsByDate(Long headquarterId);

    List<CareAndAppointmentPanelEmployeeDTO> getAppointmentsByHeadquarterId(Long headquarterId);
}
