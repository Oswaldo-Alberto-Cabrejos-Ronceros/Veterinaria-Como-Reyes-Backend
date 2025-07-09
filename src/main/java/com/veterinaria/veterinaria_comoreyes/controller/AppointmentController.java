package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.*;
import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareAndAppointmentPanelEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public AppointmentResponseDTO updateAppointment(@PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO dto) {
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

        List<TimesForTurnDTO> availableTimes = appointmentService.getAvailableTimesForTurn(headquarterVetServiceId,
                date);

        if (availableTimes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si no hay horarios
        }

        return ResponseEntity.ok(availableTimes); // 200 OK con la lista
    }

    @GetMapping("/services")
    public ResponseEntity<List<BasicServiceForAppointmentDTO>> getServicesByHeadquarterAndSpecies(
            @RequestParam Long headquarterId,
            @RequestParam Long speciesId) {

        List<BasicServiceForAppointmentDTO> services = appointmentService
                .getServicesByHeadquarterAndSpeciesForAppointment(headquarterId, speciesId);

        if (services.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(services);
    }

    @GetMapping("/client/{clientId}/panel")
    public ResponseEntity<List<InfoBasicAppointmentForPanelDTO>> getAppointmentsForClientPanel(
            @PathVariable Long clientId) {
        List<InfoBasicAppointmentForPanelDTO> appointments = appointmentService.getAppointmentsForClientPanel(clientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/search")
    public Page<AppointmentListDTO> searchAppointments(
            @RequestParam(required = false) String day,
            @RequestParam(required = false) String headquarter,
            @RequestParam(required = false) String categoryService,
            @RequestParam(required = false) String appointmentStatus,
            @PageableDefault(size = 10) Pageable pageable) {

        // Validación del formato de fecha si es necesario
        if (day != null && !day.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use YYYY-MM-DD");
        }

        return appointmentService.searchAppointments(
                day, headquarter, categoryService, appointmentStatus, pageable);
    }

    /************** PANEL ADMIN ********/

    @GetMapping("/panel-admin/by-date")
    public ResponseEntity<List<AppointmentInfoPanelAdminDTO>> getAppointmentsByDate() {
        List<AppointmentInfoPanelAdminDTO> result = appointmentService.getAppointmentsByDateForPanelAdmin();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/panel-admin/stats/today")
    public ResponseEntity<AppointmentStatsTodayDTO> getTodayAppointmentStats() {
        return ResponseEntity.ok(appointmentService.getTodayAppointmentStats());
    }


    /************** PANEL MANAGER ********/

    @GetMapping("/panel-manager/{headquarterId}/by-date")
    public ResponseEntity<List<AppointmentInfoPanelAdminDTO>> getAppointmentsForPanelManager(
            @PathVariable Long headquarterId) {
        List<AppointmentInfoPanelAdminDTO> appointments = appointmentService.getAppointmentsInfoByDateAndHeadquarter(headquarterId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/email-confirm")
    public ResponseEntity<?> confirmAppointmentByEmail(@PathVariable Long id) {
        try {
            AppointmentResponseDTO response = appointmentService.confirmAppointmentByEmail(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cita confirmada exitosamente mediante email",
                    "data", response
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("status", "error", "message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage()));
        }
    }
    @GetMapping("/panel-manager/stats/{headquarterId}/today")
    public ResponseEntity<AppointmentStatsTodayDTO> getTodayAppointmentStatsByHeadquarter(@PathVariable Long headquarterId) {
        return ResponseEntity.ok(appointmentService.getTodayAppointmentStatsByHeadquarter(headquarterId));
    }

    /************** PANEL EMPLOYEE ********/
    @GetMapping("/panel-employee/{employeeId}")
    public ResponseEntity<List<CareAndAppointmentPanelEmployeeDTO>> getAppointmentsForEmployee(@PathVariable Long employeeId) {
        List<CareAndAppointmentPanelEmployeeDTO> data = appointmentService.getAppointmentsForEmployee(employeeId);
        return ResponseEntity.ok(data);
    }
    /************** VIEW INFO APPOINTMENT ********/
    @GetMapping("/panel-info/{appointmentId}")
    public ResponseEntity<InfoAppointmentForPanelDTO> getAppointmentPanelInfo(
            @PathVariable Long appointmentId) {
        return appointmentService.getAppointmentInfoForPanel(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/panel-info/{appointmentId}/animal-info")
    public ResponseEntity<AnimalInfoForAppointmentDTO> getAnimalInfo(@PathVariable Long appointmentId) {
        return appointmentService.getAnimalInfoByAppointmentId(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/panel-info/{appointmentId}/client-info")
    public ResponseEntity<ClientInfoForAppointmentDTO> getClientInfoByAppointmentId(@PathVariable Long appointmentId) {
        return appointmentService.getClientInfoForAppointment(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/panel-info/{appointmentId}/payment-info")
    public ResponseEntity<PaymentInfoForAppointmentDTO> getPaymentInfoByAppointment(
            @PathVariable Long appointmentId) {
        return appointmentService.getPaymentInfoByAppointmentId(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /************** Panel Receptionist ********/
    @GetMapping("/panel-receptionist/stats")
    public ResponseEntity<AppointmentStatsForReceptionistDTO> getStatsByDate() {
        return ResponseEntity.ok(appointmentService.getStatsByDate());
    }

}
