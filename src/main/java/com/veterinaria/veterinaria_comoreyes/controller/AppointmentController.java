package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.*;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/available-times")
    public ResponseEntity<List<TimesForTurnDTO>> getAvailableTimes(
            @RequestParam("headquarterVetServiceId") Long headquarterVetServiceId,
            @RequestParam("date") String date) {

        List<TimesForTurnDTO> availableTimes = appointmentService.getAvailableTimesForTurn(headquarterVetServiceId, date);

        if (availableTimes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si no hay horarios
        }

        return ResponseEntity.ok(availableTimes); // 200 OK con la lista
    }

    @GetMapping("/services")
    public ResponseEntity<List<BasicServiceForAppointmentDTO>> getServicesByHeadquarterAndSpecies(
            @RequestParam Long headquarterId,
            @RequestParam Long speciesId) {

        List<BasicServiceForAppointmentDTO> services =
                appointmentService.getServicesByHeadquarterAndSpeciesForAppointment(headquarterId, speciesId);

        if (services.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(services);
    }

    @GetMapping("/client/{clientId}/panel")
    public ResponseEntity<List<InfoBasicAppointmentForPanelDTO>> getAppointmentsForClientPanel(@PathVariable Long clientId) {
        List<InfoBasicAppointmentForPanelDTO> appointments = appointmentService.getAppointmentsForClientPanel(clientId);
        return ResponseEntity.ok(appointments);
    }
}
