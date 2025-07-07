package com.veterinaria.veterinaria_comoreyes.external.email.notifications;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentNotificationDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Appointment;
import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import com.veterinaria.veterinaria_comoreyes.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AppointmentReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final EmailAppointmentNotification emailNotification;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    @Autowired
    public AppointmentReminderScheduler(AppointmentRepository appointmentRepository,
                                        EmailAppointmentNotification emailNotification) {
        this.appointmentRepository = appointmentRepository;
        this.emailNotification = emailNotification;
    }

    @Transactional
    @Scheduled(cron = "0 0 0/12 * * ?") // Cada 12 horas
    public void sendAppointmentReminders() {
        LocalDateTime tomorrowStart = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1).minusSeconds(1);

        List<Appointment> appointments = appointmentRepository
                .findByScheduleDateTimeBetweenAndStatusAppointment(
                        tomorrowStart,
                        tomorrowEnd,
                        StatusAppointment.PROGRAMADA
                );

        for (Appointment appointment : appointments) {
            try {
                // Carga explícitamente las relaciones necesarias
                Appointment loadedAppointment = appointmentRepository.findByIdWithRelations(appointment.getAppointmentId())
                        .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

                if (loadedAppointment.getAnimal() == null ||
                        loadedAppointment.getAnimal().getClient() == null ||
                        loadedAppointment.getAnimal().getClient().getUser() == null ||
                        loadedAppointment.getAnimal().getClient().getUser().getEmail() == null) {
                    continue;
                }

                AppointmentNotificationDTO dto = convertToNotificationDTO(loadedAppointment);
                emailNotification.sendAppointmentReminder(dto);

            } catch (Exception e) {
                System.err.println("Error enviando recordatorio para cita ID: " +
                        appointment.getAppointmentId() + " - " + e.getMessage());
            }
        }
    }

    private AppointmentNotificationDTO convertToNotificationDTO(Appointment appointment) {
        return AppointmentNotificationDTO.builder()
                .appointmentId(appointment.getAppointmentId())
                .scheduleDateTime(appointment.getScheduleDateTime())
                .formattedDate(appointment.getScheduleDateTime().format(DATE_FORMATTER))
                .formattedTime(appointment.getScheduleDateTime().format(TIME_FORMATTER))
                .ownerName(appointment.getAnimal().getClient().getName() + " " +
                        appointment.getAnimal().getClient().getLastName())
                .headquarterName(appointment.getHeadquarterVetService().getHeadquarter().getName())
                .vetServiceName(appointment.getHeadquarterVetService().getVeterinaryService().getName())
                .petName(appointment.getAnimal().getName())
                .ownerEmail(appointment.getAnimal().getClient().getUser().getEmail())
                .build();
    }
}