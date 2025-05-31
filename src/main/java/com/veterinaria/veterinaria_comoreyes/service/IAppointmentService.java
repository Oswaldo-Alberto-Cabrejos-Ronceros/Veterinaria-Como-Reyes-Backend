package com.veterinaria.veterinaria_comoreyes.service;

import java.util.List;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentResponseDTO;

public interface IAppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);

    void deleteAppointment(Long id);

    AppointmentResponseDTO confirmAppointment(Long id);

    AppointmentResponseDTO completeAppointment(Long id);

}
