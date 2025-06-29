package com.veterinaria.veterinaria_comoreyes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.BasicServiceForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.TimesForTurnDTO;
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

        List<TimesForTurnDTO> getAvailableTimesForTurn(Long headquarterVetServiceId, String date);

        List<BasicServiceForAppointmentDTO> getServicesByHeadquarterAndSpeciesForAppointment(Long headquarterId,
                        Long speciesId);

        Page<AppointmentListDTO> searchAppointments(
                        LocalDate scheduleDate,
                        String statusAppointment,
                        Long headquarterVetServiceId,
                        Long employeeId,
                        Long animalId,
                        Pageable pageable);
}
