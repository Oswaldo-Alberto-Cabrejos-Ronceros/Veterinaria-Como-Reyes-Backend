package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.controller;

import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.*;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.service.ClinicReportService;
import com.veterinaria.veterinaria_comoreyes.external.reports.financial.enums.ReportPeriod;
import com.veterinaria.veterinaria_comoreyes.external.reports.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/reports/clinical")
@RequiredArgsConstructor
@Slf4j
public class ClinicReportController {

    private final ClinicReportService clinicalReportService;
    private final SecurityUtil securityUtil;
    // NO

    // 1. Citas por período
    @GetMapping("/appointments/{period}")
    public ResponseEntity<List<AppointmentsByTimeDTO>> getAppointmentsByPeriod(@PathVariable ReportPeriod period) {
        return ResponseEntity.ok(clinicalReportService.getAppointmentsByPeriod(period));
    }
    // NO

    @GetMapping("/appointments/{period}/pdf")
    public ResponseEntity<byte[]> getAppointmentsByPeriodPdf(@PathVariable ReportPeriod period) {
        try {
            var data = clinicalReportService.getAppointmentsByPeriod(period);
            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            byte[] pdf = clinicalReportService.generateAppointmentsByPeriodPdf(period);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition",
                            "inline; filename=appointments_" + period.name().toLowerCase() + ".pdf")
                    .body(pdf);
        } catch (Exception e) {
            log.error("Error al generar PDF de citas por periodo", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/appointments/{period}/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> generateAppointmentsByPeriodExcel(@PathVariable ReportPeriod period) {
        byte[] excel = clinicalReportService.generateAppointmentsByPeriodExcel(period);
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=appointments.xlsx").body(excel);
    }
    // NO

    // 2. Citas por veterinario
    @GetMapping("/veterinarians")
    public ResponseEntity<List<AppointmentsByVetDTO>> getAppointmentsByVet() {
        return ResponseEntity.ok(clinicalReportService.getAppointmentsByVet());
    }

    @GetMapping(value = "/veterinarians/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateAppointmentsByVetPdf() {
        byte[] pdf = clinicalReportService.generateAppointmentsByVetPdf();
        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=vets.pdf").body(pdf);
    }
    // NO

    // 3. Servicios más solicitados
    @GetMapping("/popular-services")
    public ResponseEntity<List<PopularServicesDTO>> getPopularServices() {
        return ResponseEntity.ok(clinicalReportService.getPopularServices());
    }

    @GetMapping(value = "/popular-services/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePopularServicesPdf() {
        byte[] pdf = clinicalReportService.generatePopularServicesPdf();
        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=services.pdf").body(pdf);
    }
    // NO

    // 4. Animales más atendidos por especie o raza
    @GetMapping("/animals")
    public ResponseEntity<List<AnimalsByTypeDTO>> getAnimalsBySpecieOrBreed() {
        return ResponseEntity.ok(clinicalReportService.getAnimalsBySpecieOrBreed());
    }

    @GetMapping(value = "/animals/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateAnimalsBySpecieOrBreedPdf() {
        byte[] pdf = clinicalReportService.generateAnimalsBySpecieOrBreedPdf();
        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=animals.pdf").body(pdf);
    }
    // NO

    // 5. Citas por veterinario y por periodo
    @GetMapping("/veterinarians/by-period/{period}")
    public ResponseEntity<List<AppointmentsByVetAndPeriodDTO>> getAppointmentsByVetAndPeriod(
            @PathVariable ReportPeriod period) {
        List<AppointmentsByVetAndPeriodDTO> data = clinicalReportService.getAppointmentsByVetAndPeriod(period);
        if (data == null || data.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }
    // NO

    @GetMapping(value = "/veterinarians/by-period/{period}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateAppointmentsByVetAndPeriodPdf(@PathVariable ReportPeriod period) {
        byte[] pdf = clinicalReportService.generateAppointmentsByVetAndPeriodPdf(period);
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "inline; filename=appointments-vet-" + period.name().toLowerCase() + ".pdf")
                .body(pdf);
    }
    // NO
    @GetMapping(value = "/veterinarians/by-period/{period}/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> generateAppointmentsByVetAndPeriodExcel(@PathVariable ReportPeriod period) {
        byte[] excel = clinicalReportService.generateAppointmentsByVetAndPeriodExcel(period);
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=appointments-vet-" + period.name().toLowerCase() + ".xlsx")
                .body(excel);
    }

    // ESTE SE USARÁ GET http://localhost:8080/api/reports/clinical/veterinarians/by-headquarter/cares/pdf
    // En ClinicReportController.java
    @GetMapping("/veterinarians/by-headquarter/cares")
    public ResponseEntity<List<CaresByVetAndHeadquarterDTO>> getCaresByVetAndHeadquarter() {
        List<CaresByVetAndHeadquarterDTO> data = clinicalReportService.getCaresByVetAndHeadquarter();
        if (data == null || data.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "/veterinarians/by-headquarter/cares/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateCaresByVetAndHeadquarterPdf() {
        try {
            // 📄 Obtener datos
            List<CaresByVetAndHeadquarterDTO> data = clinicalReportService.getCaresByVetAndHeadquarter();
            if (data == null || data.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // 👤 Autor (empleado autenticado)
            Employee empleado = securityUtil.getAuthenticatedEmployee();
            String autor = empleado.getName() + " " + empleado.getLastName() + " - " +
                    (empleado.getRoles().isEmpty() ? "Empleado" : empleado.getRoles().get(0).getName());

            // 📅 Fecha de generación (ej. 13 de julio, 2025)
            String fechaGeneracion = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", new Locale("es", "PE")));

            // 🏢 Empresa
            String empresa = "Veterinaria Como Reyes";

            // 🧾 Generar PDF
            byte[] pdf = clinicalReportService.generateCaresByVetAndHeadquarterPdf(data, autor, fechaGeneracion,
                    empresa);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "inline; filename=cares_by_vet_and_headquarter.pdf")
                    .body(pdf);

        } catch (Exception e) {
            log.error("Error al generar PDF de atenciones por sede y veterinario", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
