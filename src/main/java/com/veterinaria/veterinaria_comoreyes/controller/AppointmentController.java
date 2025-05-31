package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentResponseDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    @GetMapping
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public AppointmentResponseDTO getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public AppointmentResponseDTO createAppointment(@Valid @RequestBody AppointmentRequestDTO dto) {
        return appointmentService.createAppointment(dto);
    }

    @PutMapping("/{id}")
    public AppointmentResponseDTO updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequestDTO dto) {
        return appointmentService.updateAppointment(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    @PatchMapping("/{id}/confirm")
    public AppointmentResponseDTO confirmAppointment(@PathVariable Long id) {
        return appointmentService.confirmAppointment(id);
    }

    @PatchMapping("/{id}/complete")
    public AppointmentResponseDTO completeAppointment(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }
}
