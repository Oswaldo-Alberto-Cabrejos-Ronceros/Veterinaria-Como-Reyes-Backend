package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.AppointmentResponseDTO;

import java.util.List;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);

    void deleteAppointment(Long id);

    AppointmentResponseDTO confirmAppointment(Long id);

    AppointmentResponseDTO completeAppointment(Long id);

}
